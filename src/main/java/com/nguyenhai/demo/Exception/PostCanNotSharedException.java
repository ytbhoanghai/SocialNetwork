package com.nguyenhai.demo.Exception;

public class PostCanNotSharedException extends RuntimeException {

    public PostCanNotSharedException() {
        super("this post cannot be shared");
    }
}
