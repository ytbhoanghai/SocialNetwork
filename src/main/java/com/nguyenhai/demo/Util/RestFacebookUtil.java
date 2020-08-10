package com.nguyenhai.demo.Util;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;

public class RestFacebookUtil {

    private final static String SECRET_KEY = "e844f4b8e8131e7c18800ea568f0d444";

    public static User getInfoUser(String accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, SECRET_KEY, Version.LATEST);
        return facebookClient.fetchObject("me", User.class, Parameter.with("fields", "name,email,picture.width(360).height(360)"));
    }
}
