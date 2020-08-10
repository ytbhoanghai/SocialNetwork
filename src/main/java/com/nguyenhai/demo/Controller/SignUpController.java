package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Form.SignUpForm;
import com.nguyenhai.demo.Response.SignUpSuccessResponse;
import com.nguyenhai.demo.Service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping(value = "/sign-up")
public class SignUpController {

    private SignUpService signUpService;

    @Autowired
    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @GetMapping
    public String requestPageSignUp() {
        return "sign-up";
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@Valid @RequestBody SignUpForm form) throws IOException {
        SignUpSuccessResponse response = signUpService.createAccount(form);
        return ResponseEntity.ok(response);
    }
}
