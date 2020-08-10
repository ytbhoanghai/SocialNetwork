package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Exception.InfoUserNotFoundException;
import com.nguyenhai.demo.Exception.ResourcesNotFoundException;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.InfoUpcomingBirthdayOfFriendResponse;
import com.nguyenhai.demo.Response.PhotoResponse;
import com.nguyenhai.demo.Response.PostResponse;
import com.nguyenhai.demo.Service.*;
import com.nguyenhai.demo.Util.MonitorUserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private InfoUserService infoUserService;
    private FriendRequestService friendRequestService;
    private MonitorUserStatus monitorUserStatus;
    private PostService postService;
    private PhotoGroupService photoGroupService;

    @Autowired
    public UserServiceImpl(InfoUserService infoUserService,
                           FriendRequestService friendRequestService,
                           MonitorUserStatus monitorUserStatus,
                           PostService postService,
                           PhotoGroupService photoGroupService) {

        this.infoUserService = infoUserService;
        this.friendRequestService = friendRequestService;
        this.monitorUserStatus = monitorUserStatus;
        this.postService = postService;
        this.photoGroupService = photoGroupService;

    }

    @Override
    public String findIdByEmail(String email) {
        InfoUser user = infoUserService.findByEmail(email);
        return user.getId();
    }

    @Override
    public Boolean isAccept(String id, String email) {
        InfoUser u = null;
        try {
            u = infoUserService.findById(id);
        } catch (InfoUserNotFoundException e) {
            u = infoUserService.findByEmail(id);
        }
        InfoUser me = infoUserService.findByEmail(email);

        return !u.getBlockedListFriendInfo().containsKey(me.getId());
    }

    @Override
    public InfoUser getInfoUserById(String id, String email) {
        InfoUser u = infoUserService.findById(id);
        InfoUser me = infoUserService.findByEmail(email);

        if (u.getBlockedListFriendInfo().containsKey(me.getId())) {
            throw new ResourcesNotFoundException();
        }

        return u;
    }

    @Override
    public BasicUserInfoResponse getBasicInfoById(String id, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser yo = infoUserService.findById(id);

        return buildBasicUserInfoResponse(me, yo);
    }

    @Override
    public List<BasicUserInfoResponse> getListFriends(String email, String id, Integer page, Integer number) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser user = infoUserService.findById(id);
        List<String> ids = user.getCurrentListFriendInfo().values().stream()
                .map(InfoUser.FriendInfo::getIdFriend)
                .skip(page * number).limit(number)
                .collect(Collectors.toList());

        return infoUserService.findByIdIsIn(ids).stream()
                .map(infoUser -> {
                    BasicUserInfoResponse response = buildBasicUserInfoResponse(me, infoUser);

                    MonitorUserStatus.Info info = monitorUserStatus.getInfoById(infoUser.getId());
                    if(info != null) {
                        response.setIsOnline(info.isOnline());
                    }

                    return response;
                })
                .sorted((o1, o2) -> o1.getFirstName().compareToIgnoreCase(o2.getFirstName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BasicUserInfoResponse> getListFriendsRecentlyAdded(String email, String id, Integer page, Integer number) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser user = infoUserService.findById(id);

        List<String> ids = user.getCurrentListFriendInfo().values().stream()
                .sorted((o1, o2) -> o2.getDateAddFriend().compareTo(o1.getDateAddFriend()))
                .map(InfoUser.FriendInfo::getIdFriend)
                .skip(page * number).limit(number)
                .collect(Collectors.toList());

        BasicUserInfoResponse[] result = new BasicUserInfoResponse[ids.size()];

        infoUserService.findByIdIsIn(ids).stream()
                .map((infoUser) -> buildBasicUserInfoResponse(me, infoUser))
                .forEach(e -> {
                    int index = ids.indexOf(e.getId());
                    result[index] = e;
                });

        return Arrays.asList(result);
    }

    @Override
    public List<BasicUserInfoResponse> getListFriendsBlocked(String email, String id, Integer page, Integer number) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser user = infoUserService.findById(id);

        List<String> ids = user.getBlockedListFriendInfo().values().stream()
                .map(InfoUser.FriendInfo::getIdFriend)
                .skip(page * number).limit(number)
                .collect(Collectors.toList());

        return infoUserService.findByIdIsIn(ids).stream()
                .map((infoUser -> buildBasicUserInfoResponse(me, infoUser)))
                .collect(Collectors.toList());
    }

    @Override
    public List<BasicUserInfoResponse> getListFriendsSameHometown(String email, String id, Integer page, Integer number) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser user = infoUserService.findById(id);

        List<String> idsHometownOfMe = getListIdsOfHometown(me);
        List<String> idsFriends = new ArrayList<>(user.getCurrentListFriendInfo().keySet());

        return infoUserService.findByIdIsIn(idsFriends).stream()
                .filter(infoUser -> {
                    List<String> temp = getListIdsOfHometown(infoUser);
                    for (String t : temp) {
                        if (idsHometownOfMe.contains(t)) {
                            return true;
                        }
                    }
                    return false;
                })
                .skip(number * page).limit(number)
                .map(infoUser -> buildBasicUserInfoResponse(me, infoUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<BasicUserInfoResponse> getListFollowing(String email, String id, Integer page, Integer number) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser user = infoUserService.findById(id);

        List<String>ids = user.getListFollowing().stream()
                .skip(number * page).limit(number)
                .collect(Collectors.toList());

        return infoUserService.findByIdIsIn(ids).stream()
                .map(infoUser -> buildBasicUserInfoResponse(me, infoUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<BasicUserInfoResponse> getListUserOnline(String id, Integer page, Integer number) {
        InfoUser user = infoUserService.findById(id);

        List<String> ids = user.getCurrentListFriendInfo().keySet().stream()
                .filter(s -> monitorUserStatus.getInfoById(s) != null)
                .collect(Collectors.toList());

        return infoUserService.findByIdIsIn(ids).stream()
                .map(infoUser -> {
                    BasicUserInfoResponse response = BasicUserInfoResponse.build(infoUser);

                    MonitorUserStatus.Info info = monitorUserStatus.getInfoById(infoUser.getId());
                    if (info != null) {
                        response.setIsOnline(info.isOnline());
                        response.setLastAccess(info.getLastAccess());
                    } else {
                        monitorUserStatus.setIsOffline(infoUser.getId(), MonitorUserStatus.Type.ID);
                        response.setIsOnline(false);
                        response.setLastAccess(new Date(System.currentTimeMillis()));
                    }

                    return response;
                })
                .skip(number * page).limit(number)
                .collect(Collectors.toList());

    }

    @Override
    public List<PostResponse> getPost(String id, Integer page, Integer number, String email) {
        return postService.getPost(id, page, number, email);
    }

    @Override
    public List<PostResponse> getNextPost(String idUser, String idPost, Integer number, String email) {
        return postService.getNextPost(idUser, idPost, number, email);
    }

    @Override
    public List<PostResponse> getTimeline(String idUser, String idPostStart, Integer number, String email) {
        return postService.getTimeline(idUser, idPostStart, number, email);
    }

    @Override
    public List<PhotoResponse> getPhotos(String idUser, Integer page, Integer number) {
        return photoGroupService.findPhotos(idUser, page, number);
    }

    @Override
    public InfoUpcomingBirthdayOfFriendResponse getListUpcomingBirthdayOfFriend(String idUser) {
        InfoUpcomingBirthdayOfFriendResponse response = InfoUpcomingBirthdayOfFriendResponse.getInstance();

        InfoUser user = infoUserService.findById(idUser);
        List<InfoUser> friendsList = infoUserService
                .findByIdIsIn(new ArrayList<>(user.getCurrentListFriendInfo().keySet()));

        Date now = new Date();
        LocalDate localDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();

        friendsList.forEach(infoUser -> {
            LocalDate ld = infoUser.getBirthDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int m = ld.getMonthValue();
            int d   = ld.getDayOfMonth();

            if (d == day && m == month) {
                response.addToday(infoUser);
            } else if (m == getMonth(month) && d > day) {
                response.addMonth(String.valueOf(getMonth(month)), infoUser);
            } else if (m == getMonth(month + 1)) {
                response.addMonth(String.valueOf(getMonth(month + 1)), infoUser);
            } else if (m == getMonth(month + 2)) {
                response.addMonth(String.valueOf(getMonth(month + 2)), infoUser);
            } else if (m == getMonth(month + 3)) {
                response.addMonth(String.valueOf(getMonth(month + 3)), infoUser);
            } else if (m == getMonth(month + 4)) {
                response.addMonth(String.valueOf(getMonth(month + 4)), infoUser);
            } else if (m == getMonth(month + 5)) {
                response.addMonth(String.valueOf(getMonth(month + 5)), infoUser);
            } else if (m == getMonth(month + 6)) {
                response.addMonth(String.valueOf(getMonth(month + 6)), infoUser);
            } else if (m == getMonth(month + 7)) {
                response.addMonth(String.valueOf(getMonth(month + 7)), infoUser);
            }
        });

        return response;
    }

    private int getMonth(int month) {
        return month > 12 ? month - 12 : month;
    }

    private List<String> getListIdsOfHometown(InfoUser user) {
        List<String> idsHometownOfMe = new ArrayList<>();
        if (user.getHomeTown() != null) { idsHometownOfMe.add(user.getHomeTown().getId()); }
        if (user.getCurrentCity() != null) { idsHometownOfMe.add(user.getCurrentCity().getId()); }
        if (!user.getOtherPlacesLived().isEmpty()) { idsHometownOfMe.addAll(user.getOtherPlacesLived().keySet()); }
        return idsHometownOfMe;
    }

    private BasicUserInfoResponse buildBasicUserInfoResponse(InfoUser me, InfoUser infoUser) {
        String status = friendRequestService.getStatus(me, infoUser.getId());
        Boolean isFollowing = me.getListFollowing().contains(infoUser.getId());

        BasicUserInfoResponse response = BasicUserInfoResponse.build(infoUser);

        response.setStatusForMe(status);
        response.setIsFollowing(isFollowing);

        return response;
    }
}
