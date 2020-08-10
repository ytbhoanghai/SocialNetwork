package com.nguyenhai.demo.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nguyenhai.demo.Entity.Account;
import com.nguyenhai.demo.Form.SignUpForm;
import com.nguyenhai.demo.Response.SignUpSuccessResponse;
import com.restfb.types.User;

import java.io.IOException;

public interface SignUpService {

    SignUpSuccessResponse createAccount(SignUpForm form) throws IOException;

    SignUpSuccessResponse createAccount(User user) throws IOException;

    SignUpSuccessResponse createAccount(GoogleIdToken.Payload payload) throws IOException;

    SignUpSuccessResponse createAccount(String id, String email, String password, String firstName, String lastName, Account.TypeLogin typeLogin, String urlAvatar) throws IOException;

}
