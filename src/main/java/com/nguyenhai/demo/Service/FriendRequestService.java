package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.FriendRequest;
import com.nguyenhai.demo.Entity.InfoUser;

public interface FriendRequestService {

    // status
    String ME = "me";
    String BLOCK = "block";
    String FRIEND = "friend";
    String NO_FRIEND = "no friend";
    String WAITING_1 = "waiting 1";
    String WAITING_2 = "waiting 2";

    void createFriendRequest(InfoUser me, InfoUser yo);

    void deleteFriendRequest(InfoUser me, InfoUser yo);

    FriendRequest save(FriendRequest friendRequest);

    FriendRequest findById(String id);

    String getStatus(InfoUser infoUser, String id);

}
