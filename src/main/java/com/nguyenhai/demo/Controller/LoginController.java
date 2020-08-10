package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Response.SignInSuccessResponse;
import com.nguyenhai.demo.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String requestPageLogin() {
        return "sign-in";
    }

    @PostMapping(value = "facebook", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginWithFacebook(@RequestParam String accessToken) throws IOException {
        SignInSuccessResponse response = loginService.loginWithFacebook(accessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "google", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginWithGoogle(@RequestParam String idToken) throws GeneralSecurityException, IOException {
        SignInSuccessResponse response = loginService.loginWithGoogle(idToken);
        return ResponseEntity.ok(response);
    }

}
