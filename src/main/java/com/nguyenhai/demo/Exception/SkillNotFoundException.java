package com.nguyenhai.demo.Exception;

public class SkillNotFoundException extends RuntimeException {

    public SkillNotFoundException(String id) {
        super("skill not found by id " + id);
    }
}
