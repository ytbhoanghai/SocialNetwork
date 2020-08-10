package com.nguyenhai.demo.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.SignInSuccessResponse;
import com.nguyenhai.demo.Service.InfoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Component(value = "authenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private InfoUserService infoUserService;

    @Autowired
    public CustomAuthenticationSuccessHandler(InfoUserService infoUserService) {
        this.infoUserService = infoUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        InfoUser infoUser = findByEmail(authentication.getName());
        SignInSuccessResponse response = new SignInSuccessResponse(infoUser.getId(), "/", new Date(System.currentTimeMillis()));

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(HttpStatus.OK.value());

        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.write(new ObjectMapper().writeValueAsString(response));
        printWriter.flush();
    }

    private InfoUser findByEmail(String email) {
        return infoUserService.findByEmail(email);
    }
}
