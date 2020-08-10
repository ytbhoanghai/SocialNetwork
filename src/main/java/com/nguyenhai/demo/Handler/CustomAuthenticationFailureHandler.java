package com.nguyenhai.demo.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyenhai.demo.Response.EntityExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Component(value = "authenticationFailureHandler")
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Date dateCreate = new Date(System.currentTimeMillis());

        EntityExceptionResponse response = new EntityExceptionResponse(message, status, dateCreate);

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.write(new ObjectMapper().writeValueAsString(response));
        printWriter.flush();
    }
}
