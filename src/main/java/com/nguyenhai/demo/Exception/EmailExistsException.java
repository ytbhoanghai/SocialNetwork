package com.nguyenhai.demo.Exception;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String email) {
        super("email " + email + " already exists");
    }
}
