package com.nguyenhai.demo.Exception;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String id) {
        super("notification not found by id " + id);
    }

    public NotificationNotFoundException() {
        super("notification not found");
    }
}
