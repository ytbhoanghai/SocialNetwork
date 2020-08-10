package com.nguyenhai.demo.Exception;

public class FriendRequestNotFoundException extends RuntimeException {

    public FriendRequestNotFoundException(String id) {
        super("friend request not found by id " + id);
    }
}
