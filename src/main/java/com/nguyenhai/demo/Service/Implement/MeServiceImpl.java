package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.*;
import com.nguyenhai.demo.Exception.FriendRequestNotFoundException;
import com.nguyenhai.demo.Form.PostForm;
import com.nguyenhai.demo.Repository.FeelingRepository;
import com.nguyenhai.demo.Repository.NotificationRepository;
import com.nguyenhai.demo.Repository.PlaceLivedRepository;
import com.nguyenhai.demo.Repository.PostRepository;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.CardHistoryResponse;
import com.nguyenhai.demo.Response.FriendRequestResponse;
import com.nguyenhai.demo.Response.NotificationResponse;
import com.nguyenhai.demo.Service.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

// SERVICE DIRECT OF CONTROLLER
@Service(value = "meService")
public class MeServiceImpl implements MeService {

    private InfoUserService infoUserService;
    private AccountService accountService;
    private LoginService loginService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private HistoryService historyService;


    @Autowired
    public MeServiceImpl(InfoUserService infoUserService,
                         AccountService accountService,
                         LoginService loginService,
                         FriendRequestService friendRequestService,
                         NotificationService notificationService,
                         HistoryService historyService) {

        this.infoUserService = infoUserService;
        this.accountService = accountService;
        this.loginService = loginService;
        this.friendRequestService = friendRequestService;
        this.notificationService = notificationService;
        this.historyService = historyService;
    }

    @Override
    public List<BasicUserInfoResponse> getListBasicFriendInfoByTerm(String term, String email) {
        InfoUser infoUser = infoUserService.findByEmail(email);
        List<String> idsFriends = new ArrayList<>(infoUser.getCurrentListFriendInfo().keySet());
        return infoUserService.findByIdIsIn(idsFriends).stream()
                .filter(e -> {
                    String fullName = (e.getLastName() + " " + e.getFirstName()).toLowerCase();
                    return fullName.contains(term.toLowerCase());
                })
                .map(BasicUserInfoResponse::build)
                .collect(Collectors.toList());
    }

    @Override
    public List<BasicUserInfoResponse> getBasicInfoOfOtherPeopleMayBeKnow(String email, Integer maxSize) {
        InfoUser user = infoUserService.findByEmail(email);
        List<String> idsMyFriends = new ArrayList<>(
                user.getCurrentListFriendInfo().keySet());

        idsMyFriends.add(user.getId());

        // country, collegeInfos, workPlaceInfos, currentCity, homeTown, otherPlacesLived, have at least one mutual friend, not in list friend request mutual
        List<InfoUser> notFriends = infoUserService.findAllByIdIsNotIn(idsMyFriends).stream()
                .filter(infoUser -> {
                    FriendRequest ofUser1 = friendRequestService.findById(user.getId());
                    FriendRequest ofUser2 = friendRequestService.findById(infoUser.getId());

                    return !ofUser1.getData().containsKey(ofUser2.getId())
                            && !ofUser2.getData().containsKey(ofUser1.getId()); })
                .collect(Collectors.toList());

        List<InfoUser> result = new ArrayList<>();

        notFriends.forEach(infoUser -> {
            if (infoUser.getCountry() != null && user.getCountry() != null) {
                if (infoUser.getCountry().equals(user.getCountry())) {
                    result.add(infoUser);
                }
            } else if (infoUser.getCurrentCity() != null && user.getCurrentCity() != null) {
                if (infoUser.getCurrentCity().equals(user.getCurrentCity())) {
                    result.add(infoUser);
                }
            } else if (infoUser.getHomeTown() != null && user.getHomeTown() != null) {
                if (infoUser.getHomeTown().equals(user.getHomeTown())) {
                    result.add(infoUser);
                }
            } else {
                if (existsLeastOneCollegeInTheCollegeList(infoUser, user)
                    || existsLeastOneWorkPlaceInTheWorkPlaceList(infoUser, user)
                    || existsLeastOneOtherPlacesLivedInTheOtherPlacesLivedList(infoUser, user)
                    || haveLeastOneMutualFriend(infoUser, user)) {
                    result.add(infoUser);
                }
            }
        });

        if (result.size() > maxSize) {
            Collections.shuffle(result);
            return result.subList(0, maxSize).stream()
                    .map(BasicUserInfoResponse::build)
                    .collect(Collectors.toList());
        }

        return result.stream()
                .map(BasicUserInfoResponse::build)
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendRequestResponse> getFriendRequests(String email, Integer page, Integer limit) {
        InfoUser me = infoUserService.findByEmail(email);
        FriendRequest friendRequest = friendRequestService.findById(me.getId());

        return friendRequest.getData().values().stream()
                .sorted((o1, o2) -> o2.getDateRequest().compareTo(o1.getDateRequest()))
                .skip(page * limit).limit(limit)
                .map(request -> {
                    InfoUser infoUser = infoUserService.findById(request.getId());
                    return FriendRequestResponse.build(infoUser, request.getViewed());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendRequestResponse> getNextFriendRequest(String email, String id, Integer number) {
        InfoUser me = infoUserService.findByEmail(email);
        FriendRequest friendRequest = friendRequestService.findById(me.getId());

        List<FriendRequestResponse> responses = new ArrayList<>();
        if (friendRequest.getData().containsKey(id)) {
            FriendRequest.Request request = friendRequest.getData().get(id);

            List<FriendRequest.Request> requestList = friendRequest.getData().values().stream()
                    .sorted((o1, o2) -> o2.getDateRequest().compareTo(o1.getDateRequest()))
                    .collect(Collectors.toList());

            int index = requestList.indexOf(request);

            for (int i = index + 1; i < requestList.size() && i < index + number + 1; i++) {
                InfoUser infoUser = infoUserService.findById(requestList.get(i).getId());
                responses.add(FriendRequestResponse.build(infoUser, requestList.get(i).getViewed()));
            }
        } else {
            throw new FriendRequestNotFoundException(id);
        }

        return responses;
    }

    @Override
    public InfoUser getInfoUserByEmail(String email) {
        return infoUserService.findByEmail(email);
    }

    @Override
    public BasicUserInfoResponse getBasicInfoByMe(String email) {
        InfoUser infoUser = infoUserService.findByEmail(email);
        return BasicUserInfoResponse.build(infoUser);
    }

    @Override
    public Boolean verifyAccount(String code) {
        String id = loginService.authenticationAndLogin(code);
        if (id != null) {
            InfoUser iuMe = infoUserService.findById(id);
            Account accMe = accountService.findById(id);

            iuMe.setIsValidEmail(true);
            accMe.setIsValidEmail(true);

            infoUserService.update(iuMe);
            accountService.update(accMe);

            return true;
        }
        return false;
    }

    @Override
    public List<CardHistoryResponse> getHistory(Integer page, Integer number, String email) {
        return historyService.getHistory(page, number, email);
    }

    @Override
    public List<CardHistoryResponse> getHistoryNext(String id, Integer number, String email) {
        return historyService.getHistoryNext(id, number, email);
    }

    @Override
    public void addFriendRequest(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        friendRequestService.createFriendRequest(me, yo);
        notificationService.notificationNewFriendRequest(me.getId(), yo.getEmail());
    }

    @Override
    public void deleteFriendRequest(String id, String email, Boolean notify) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        friendRequestService.deleteFriendRequest(me, yo);
        if (notify) {
            notificationService.notificationDeleteFriendRequest(me.getId(), yo.getEmail());
            notificationService.notificationDeleteFriendRequest(yo.getId(), me.getEmail());
        }
    }

    @Override
    public String getFriendStatus(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        return friendRequestService.getStatus(me, id);
    }

    @Override
    public void acceptFriendRequest(String id, String email, Boolean notify) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        FriendRequest frMe = friendRequestService.findById(me.getId());
        if (!frMe.getData().containsKey(yo.getId())) {
            throw new FriendRequestNotFoundException(yo.getId());
        }
        addFriend(me, yo);
        addFriend(yo, me);

        frMe.getData().remove(yo.getId());
        friendRequestService.save(frMe);

        infoUserService.update(me);
        infoUserService.update(yo);

        if (notify) {
            notificationService.notificationDeleteFriendRequest(yo.getId(), me.getEmail());
        }
    }

    @Override
    public HashMap<String, String> getDetailsFriendRequest(String email) {
        InfoUser me = infoUserService.findByEmail(email);
        FriendRequest friendRequest = friendRequestService.findById(me.getId());

        HashMap<String, String> result = new HashMap<>();
        Integer sum = friendRequest.getData().size();
        Integer unViewed = Long.valueOf(friendRequest.getData().entrySet().stream()
                .filter(e -> !e.getValue().getViewed())
                .count()).intValue();
        Integer viewed = sum - unViewed;

        result.put("sum", String.valueOf(sum));
        result.put("unViewed", String.valueOf(unViewed));
        result.put("viewed", String.valueOf(viewed));


        return result;
    }

    @Override
    public long updateNumberOfFriendRequestViewed(List<String> ids, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        FriendRequest friendRequest = friendRequestService.findById(me.getId());

        List<InfoUser> infoUsers = infoUserService.findByIdIsIn(ids);
        infoUsers.forEach(e -> friendRequest.getData().get(e.getId()).setViewed(true));

        return friendRequestService.save(friendRequest).getData().entrySet().stream()
                .filter(e -> !e.getValue().getViewed())
                .count();
    }

    @Override
    public void unFriend(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        unFriend(me, yo);
        unFriend(yo, me);

        infoUserService.update(me);
        infoUserService.update(yo);
    }

    @Override
    public void addFollowing(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        if (!me.getListFollowing().contains(yo.getId())) {
            me.getListFollowing().add(yo.getId());
            yo.getListFollower().add(me.getId());
        }

        infoUserService.update(me);
        infoUserService.update(yo);
    }

    @Override
    public void deleteFollowing(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        if (me.getListFollowing().contains(yo.getId())) {
            me.getListFollowing().removeIf(s -> s.equals(yo.getId()));
            yo.getListFollower().removeIf(s -> s.equals(me.getId()));
        }

        infoUserService.update(me);
        infoUserService.update(yo);
    }

    @Override
    public void addToBlockList(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        InfoUser.FriendInfo friendInfo = me.getCurrentListFriendInfo()
                .get(yo.getId());

        me.getBlockedListFriendInfo().put(yo.getId(), friendInfo);

        unFriend(me, yo);
        unFriend(yo, me);

        infoUserService.update(me);
        infoUserService.update(yo);
    }

    @Override
    public void unBlockUserById(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        Map<String, InfoUser.FriendInfo> data = me.getBlockedListFriendInfo();
        Assert.isTrue(data.containsKey(yo.getId()), "user " + id + " not exists in the block list");

        addFriend(me, yo);
        addFriend(yo, me);

        data.remove(yo.getId());

        infoUserService.update(me);
        infoUserService.update(yo);
    }

    private boolean existsLeastOneCollegeInTheCollegeList(InfoUser user1, InfoUser user2) {
        return user1.getCollegeInfos().keySet().stream()
                .anyMatch(s -> user2.getCollegeInfos().containsKey(s));
    }

    private boolean existsLeastOneWorkPlaceInTheWorkPlaceList(InfoUser user1, InfoUser user2) {
        return user1.getWorkPlaceInfos().keySet().stream()
                .anyMatch(s -> user2.getWorkPlaceInfos().containsKey(s));
    }

    private boolean existsLeastOneOtherPlacesLivedInTheOtherPlacesLivedList(InfoUser user1, InfoUser user2) {
        return user1.getOtherPlacesLived().keySet().stream()
                .anyMatch(s -> user2.getOtherPlacesLived().containsKey(s));
    }

    private boolean haveLeastOneMutualFriend(InfoUser user1, InfoUser user2) {
        return user1.getCurrentListFriendInfo().keySet().stream()
                .anyMatch(s -> user2.getCurrentListFriendInfo().containsKey(s));
    }

    private void unFriend(InfoUser iu1, InfoUser iu2) {
        iu1.getCurrentListFriendInfo().remove(iu2.getId());
        iu1.getListFollower().removeIf(s -> s.equals(iu2.getId()));
        iu1.getListFollowing().removeIf(s -> s.equals(iu2.getId()));
    }

    private void addFriend(InfoUser iu1, InfoUser iu2) {
        InfoUser.FriendInfo friendInfo = new InfoUser.FriendInfo(iu2.getId(), new Date(System.currentTimeMillis()));
        iu1.getCurrentListFriendInfo().put(friendInfo.getIdFriend(), friendInfo);
        iu1.getListFollowing().add(iu2.getId());
        iu1.getListFollower().add(iu2.getId());
    }
}
