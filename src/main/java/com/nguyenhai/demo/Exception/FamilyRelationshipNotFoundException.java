package com.nguyenhai.demo.Exception;

public class FamilyRelationshipNotFoundException extends RuntimeException {

    public FamilyRelationshipNotFoundException(String id) {
        super("family relationship not found by id " + id);
    }
}
