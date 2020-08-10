package com.nguyenhai.demo.Exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String value) {
        super("account not found by " + value);
    }
}
