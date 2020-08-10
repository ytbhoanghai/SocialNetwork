package com.nguyenhai.demo.Exception;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String fileName) {
        super("file not found by name " + fileName);
    }
}
