package com.nguyenhai.demo.Exception;

public class JobPositionNotFoundException extends RuntimeException {

    public JobPositionNotFoundException(String id) {
        super("job position not found by id " + id);
    }
}
