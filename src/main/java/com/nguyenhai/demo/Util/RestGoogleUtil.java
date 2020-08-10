package com.nguyenhai.demo.Util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.nguyenhai.demo.Exception.InvalidIDTokenException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class RestGoogleUtil {

    private static final String CLIENT_ID = "661810666435-1iqtsob5etcmben2ieu6uqaspob17fpa.apps.googleusercontent.com";

    public static GoogleIdToken getGoogleIdToken(String idToken) throws GeneralSecurityException, IOException {
        NetHttpTransport netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);
        if (googleIdToken == null) {
            throw new InvalidIDTokenException(idToken);
        }

        return googleIdToken;
    }
}
