package com.nguyenhai.demo.Exception;

public class ResourcesNotFoundException extends RuntimeException {

    public ResourcesNotFoundException() {
        super("resources not found");
    }
}
