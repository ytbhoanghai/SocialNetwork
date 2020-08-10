package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.FriendRequest;
import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Exception.FriendRequestNotFoundException;
import com.nguyenhai.demo.Repository.FriendRequestRepository;
import com.nguyenhai.demo.Service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;

@Service(value = "friendRequestService")
public class FriendRequestServiceImpl implements FriendRequestService {

    private FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    @Override
    public void createFriendRequest(InfoUser me, InfoUser yo) {
        boolean flag1 = me.getCurrentListFriendInfo().containsKey(yo.getId());
        Assert.isTrue(!flag1, "you two have made friends");

        boolean flag2 = me.getBlockedListFriendInfo().containsKey(yo.getId());
        Assert.isTrue(!flag2, "this person is on your block list");

        boolean flag3 = yo.getBlockedListFriendInfo().containsKey(me.getId());
        Assert.isTrue(!flag3, "user not found by id " + yo.getId());

        FriendRequest friendRequest = friendRequestRepository.findById(yo.getId())
                .orElseThrow(() -> new FriendRequestNotFoundException(me.getId()));
        friendRequest.getData().put(me.getId(), new FriendRequest.Request(me.getId(), false, new Date(System.currentTimeMillis())));

        save(friendRequest);
    }

    @Override
    public void deleteFriendRequest(InfoUser me, InfoUser yo) {
        FriendRequest friendRequest1 = friendRequestRepository.findById(me.getId())
                .orElseThrow(() -> new FriendRequestNotFoundException(me.getId()));
        FriendRequest friendRequest2 = friendRequestRepository.findById(yo.getId())
                .orElseThrow(() -> new FriendRequestNotFoundException(yo.getId()));

        boolean flag = friendRequest1.getData().containsKey(yo.getId()) || friendRequest2.getData().containsKey(me.getId());
        Assert.isTrue(flag, "friend request not found");

        friendRequest1.getData().remove(yo.getId());
        friendRequest2.getData().remove(me.getId());

        save(friendRequest1);
        save(friendRequest2);
    }

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        return friendRequestRepository.save(friendRequest);
    }

    @Override
    public FriendRequest findById(String id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestNotFoundException(id));
    }

    @Override
    public String getStatus(InfoUser infoUser, String id) {
        if (infoUser.getId().equals(id)) {
            return ME;
        }
        else if (infoUser.getBlockedListFriendInfo().containsKey(id)) {
            return BLOCK;
        }
        else if (infoUser.getCurrentListFriendInfo().containsKey(id)) {
            return FRIEND;
        } else {
            FriendRequest friendRequest1 = findById(infoUser.getId());
            if (friendRequest1.getData().containsKey(id)) {
                return WAITING_2;
            }
            FriendRequest friendRequest2 = findById(id);
            if (friendRequest2.getData().containsKey(infoUser.getId())) {
                return WAITING_1;
            }
        }
        return NO_FRIEND;
    }
}
