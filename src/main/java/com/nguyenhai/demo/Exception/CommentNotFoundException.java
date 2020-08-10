package com.nguyenhai.demo.Exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(String id) {
        super("comment not found by id " + id);
    }
}
