package com.nguyenhai.demo.Exception;

public class PlaceLivedNotFoundException extends RuntimeException {

    public PlaceLivedNotFoundException(String id) {
        super("place lived not found by id " + id);
    }
}
