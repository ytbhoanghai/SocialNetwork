package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.Notification;
import com.nguyenhai.demo.Form.PostForm;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.CardHistoryResponse;
import com.nguyenhai.demo.Response.FriendRequestResponse;
import com.nguyenhai.demo.Response.NotificationResponse;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.List;

public interface MeService {

    List<BasicUserInfoResponse> getListBasicFriendInfoByTerm(String term, String email);

    List<BasicUserInfoResponse> getBasicInfoOfOtherPeopleMayBeKnow(String email, Integer maxSize);

    List<FriendRequestResponse> getFriendRequests(String email, Integer page, Integer limit);

    List<FriendRequestResponse> getNextFriendRequest(String email, String id, Integer number);

    BasicUserInfoResponse getBasicInfoByMe(String email);

    String getFriendStatus(String id, String email);

    HashMap<String, String> getDetailsFriendRequest(String email);

    long updateNumberOfFriendRequestViewed(List<String> ids, String email);

    InfoUser getInfoUserByEmail(String email);

    Boolean verifyAccount(String code);

    List<CardHistoryResponse> getHistory(Integer page, Integer number, String email);

    List<CardHistoryResponse> getHistoryNext(String id, Integer number, String email);

    void addFriendRequest(String id, String email);

    void deleteFriendRequest(String id, String email, Boolean notify);

    void acceptFriendRequest(String id, String email, Boolean notify);

    void unFriend(String id, String email);

    void addFollowing(String id, String email);

    void deleteFollowing(String id, String email);

    void addToBlockList(String id, String email);

    void unBlockUserById(String id, String email);

}
