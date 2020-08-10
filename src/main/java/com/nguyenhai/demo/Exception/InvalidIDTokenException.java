package com.nguyenhai.demo.Exception;

public class InvalidIDTokenException extends RuntimeException {

    public InvalidIDTokenException(String idToken) {
        super("Invalid id token " + idToken.substring(0, 15) + "...");
    }
}
