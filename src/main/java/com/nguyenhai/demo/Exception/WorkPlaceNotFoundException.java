package com.nguyenhai.demo.Exception;

public class WorkPlaceNotFoundException extends RuntimeException {

    public WorkPlaceNotFoundException(String id) {
        super("work place not found by id " + id);
    }
}
