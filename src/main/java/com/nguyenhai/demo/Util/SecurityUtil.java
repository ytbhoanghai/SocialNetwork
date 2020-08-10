package com.nguyenhai.demo.Util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class SecurityUtil {

    public static void setAuthentication(UserDetails userDetails) {
        HttpServletRequest request = getHttpServletRequest(); // get httpServletRequest

         setRememberMeForClient(request); // setRememberMe - always = true

        // set authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
    }

    private static void setRememberMeForClient(HttpServletRequest httpServletRequest) {
        httpServletRequest.setAttribute(SpringSessionRememberMeServices.REMEMBER_ME_LOGIN_ATTR, true);
        httpServletRequest.getSession().setMaxInactiveInterval(2592000);
    }
}
