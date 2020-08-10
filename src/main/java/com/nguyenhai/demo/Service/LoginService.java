package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Response.SignInSuccessResponse;
import com.nguyenhai.demo.Response.SignUpSuccessResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface LoginService {

    String PREFIX_VERIFY = "ytb:verify:";
    Integer TIME_VERIFICATION_CODE_EXPIRES = 30;

    SignInSuccessResponse loginWithFacebook(String accessToken) throws IOException;

    SignInSuccessResponse loginWithGoogle(String idToken) throws GeneralSecurityException, IOException;

    String authenticationAndLogin(String code);

    String getCodeVerify(String idUser);
}
