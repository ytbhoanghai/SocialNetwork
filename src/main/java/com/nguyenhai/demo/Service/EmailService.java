package com.nguyenhai.demo.Service;

import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

public interface EmailService {

    String FILE_NAME_EMAIL_CHANGE_PASSWORD = "change-password";
    String FILE_NAME_EMAIL_VERIFY = "verify";

    @Async("sendEmailExecutor")
    void mailChangePassword(String email) throws IOException;

    @Async("sendEmailExecutor")
    void mailVerify(String email) throws IOException;
}
