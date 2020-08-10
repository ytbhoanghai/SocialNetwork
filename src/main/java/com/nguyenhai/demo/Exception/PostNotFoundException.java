package com.nguyenhai.demo.Exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String id) {
        super("post not found by id " + id);
    }
}
