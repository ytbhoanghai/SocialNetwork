package com.nguyenhai.demo.Exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String id) {
        super("message not found by id " + id);
    }
}
