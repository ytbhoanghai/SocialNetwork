package com.nguyenhai.demo.Exception;

public class CodeChangePasswordNotFoundException extends RuntimeException {

    public CodeChangePasswordNotFoundException(String id) {
        super("not found code change password by id " + id);
    }
}
