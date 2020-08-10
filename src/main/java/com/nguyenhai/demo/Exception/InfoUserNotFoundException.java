package com.nguyenhai.demo.Exception;

public class InfoUserNotFoundException extends RuntimeException {

    public InfoUserNotFoundException() {
        super("info user not found ");
    }

    public InfoUserNotFoundException(String email) {
        super("info user not found by email " + email);
    }
}
