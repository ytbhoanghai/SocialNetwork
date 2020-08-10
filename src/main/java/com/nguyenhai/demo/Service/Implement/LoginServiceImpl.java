package com.nguyenhai.demo.Service.Implement;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Exception.InfoUserNotFoundException;
import com.nguyenhai.demo.Response.SignInSuccessResponse;
import com.nguyenhai.demo.Response.SignUpSuccessResponse;
import com.nguyenhai.demo.Security.CustomUserDetailsService;
import com.nguyenhai.demo.Service.InfoUserService;
import com.nguyenhai.demo.Service.LoginService;
import com.nguyenhai.demo.Service.SignUpService;
import com.nguyenhai.demo.Util.RestFacebookUtil;
import com.nguyenhai.demo.Util.RestGoogleUtil;
import com.nguyenhai.demo.Util.SecurityUtil;
import com.restfb.types.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.AuthenticationFailedException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// SERVICE DIRECT OF CONTROLLER
@Service(value = "loginService")
public class LoginServiceImpl implements LoginService {

    private InfoUserService infoUserService;
    private UserDetailsService userDetailsService;
    private SignUpService signUpService;
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public LoginServiceImpl(InfoUserService infoUserService,
                            UserDetailsService userDetailsService,
                            SignUpService signUpService,
                            RedisTemplate<Object, Object> redisTemplate) {

        this.infoUserService = infoUserService;
        this.userDetailsService = userDetailsService;
        this.signUpService = signUpService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public SignInSuccessResponse loginWithFacebook(String accessToken) throws IOException {
        User user = RestFacebookUtil.getInfoUser(accessToken);

        UserDetails userDetails = null;
        InfoUser infoUser = null;
        SignInSuccessResponse response = null;

        try {
            Assert.isTrue(user.getEmail() != null, "We realize you haven't linked your email to this account yet.");

            infoUser = infoUserService.findById(user.getId());
            userDetails = ((CustomUserDetailsService) userDetailsService).buildUserDetails(infoUser.getEmail());
        } catch (InfoUserNotFoundException e) {
            SignUpSuccessResponse signUpSuccessResponse = signUpService.createAccount(user);
            infoUser = infoUserService.findById(signUpSuccessResponse.getId());
            userDetails = ((CustomUserDetailsService) userDetailsService).buildUserDetails(infoUser.getEmail());
        }

        response = new SignInSuccessResponse(infoUser.getId(), "/", new Date(System.currentTimeMillis()));
        SecurityUtil.setAuthentication(userDetails);
        return response;
    }

    @Override
    public SignInSuccessResponse loginWithGoogle(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken googleIdToken = RestGoogleUtil.getGoogleIdToken(idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        UserDetails userDetails = null;
        InfoUser infoUser = null;
        SignInSuccessResponse response = null;

        try {
            infoUser = infoUserService.findById(payload.getSubject());
            userDetails = ((CustomUserDetailsService) userDetailsService).buildUserDetails(payload.getEmail());
        } catch (InfoUserNotFoundException e) {
            SignUpSuccessResponse signUpSuccessResponse = signUpService.createAccount(payload);
            infoUser = infoUserService.findById(signUpSuccessResponse.getId());
            userDetails = ((CustomUserDetailsService) userDetailsService).buildUserDetails(infoUser.getEmail());
        }

        response = new SignInSuccessResponse(infoUser.getId(), "/", new Date(System.currentTimeMillis()));
        SecurityUtil.setAuthentication(userDetails);
        return response;
    }

    @Override
    public String authenticationAndLogin(String code) {
        Object obj = redisTemplate.opsForValue().get(buildKeyVerify(code));
        if (obj != null) {
            InfoUser me = infoUserService.findById((String) obj);
            UserDetails userDetails = ((CustomUserDetailsService) userDetailsService).buildUserDetails(me.getEmail());
            SecurityUtil.setAuthentication(userDetails);
            return (String) obj;
        }
        return null;
    }

    @Override
    public String getCodeVerify(String idUser) {
        String code = RandomStringUtils.randomAlphanumeric(18);
        String key = buildKeyVerify(code);

        redisTemplate.opsForValue().set(key, idUser);
        redisTemplate.expire(key, TIME_VERIFICATION_CODE_EXPIRES, TimeUnit.MINUTES);

        return code;
    }

    private String buildKeyVerify(String code) {
        return PREFIX_VERIFY + code;
    }

}
