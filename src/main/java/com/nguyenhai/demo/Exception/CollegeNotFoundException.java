package com.nguyenhai.demo.Exception;

public class CollegeNotFoundException extends RuntimeException {

    public CollegeNotFoundException(String id) {
        super("college not found by id " + id);
    }
}
