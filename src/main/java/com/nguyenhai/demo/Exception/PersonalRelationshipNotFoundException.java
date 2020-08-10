package com.nguyenhai.demo.Exception;

public class PersonalRelationshipNotFoundException extends RuntimeException {

    public PersonalRelationshipNotFoundException(String id) {
        super("personal relationship not found by id " + id);
    }
}
