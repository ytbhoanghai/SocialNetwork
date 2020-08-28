package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.InfoUpcomingBirthdayOfFriendResponse;
import com.nguyenhai.demo.Response.PhotoResponse;
import com.nguyenhai.demo.Response.PostResponse;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    String findIdByEmail(String email);

    Boolean isAccept(String id, String email);

    InfoUser getInfoUserById(String id, String email);

    BasicUserInfoResponse getBasicInfoById(String id, String email);

    List<BasicUserInfoResponse> getListFriends(String email, String id, Integer page, Integer number);

    List<BasicUserInfoResponse> getListFriendsRecentlyAdded(String email, String id, Integer page, Integer number);

    List<BasicUserInfoResponse> getListFriendsBlocked(String email, String id, Integer page, Integer number);

    List<BasicUserInfoResponse> getListFriendsSameHometown(String email, String id, Integer page, Integer number);

    List<BasicUserInfoResponse> getListFollowing(String email, String id, Integer page, Integer number);

    List<BasicUserInfoResponse> getListUserOnline(String id, Integer page, Integer number);

    List<BasicUserInfoResponse> getListBasicFriendInfoByTerm(String term, String email);

    List<PostResponse> getPost(String id, Integer page, Integer number, String email);

    List<PostResponse> getNextPost(String idUser, String idPost, Integer number, String email);

    List<PostResponse> getTimeline(String idUser, String idPostStart, Integer number, String email);

    List<PhotoResponse> getPhotos(String idUser, Integer page, Integer number);

    InfoUpcomingBirthdayOfFriendResponse getListUpcomingBirthdayOfFriend(String idUser);
}
