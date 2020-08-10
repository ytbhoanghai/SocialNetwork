package com.nguyenhai.demo.Exception;

public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException(String id) {
        super("country not found by id " + id);
    }
}
