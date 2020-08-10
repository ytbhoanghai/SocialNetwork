package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Annotation.Email;
import com.nguyenhai.demo.Service.AccountService;
import com.nguyenhai.demo.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(value = "/email")
public class EmailController {

    private EmailService emailService;
    private AccountService accountService;

    @Autowired
    public EmailController(EmailService emailService, AccountService accountService) {
        this.emailService = emailService;
        this.accountService = accountService;
    }

    @GetMapping(value = "verify", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> requestEmailVerifyAccount(Principal principal) throws IOException {
        String email = principal.getName();
        emailService.mailVerify(email);
        return ResponseEntity.ok("emails will be sent in about 1 minutes, please check in your mail box");
    }

    @GetMapping(value = "change-password", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> requestMailChangePassword(@Valid @Email @RequestParam(required = false, defaultValue = "ytb") String email, Principal principal) throws IOException {
        if (email.equals("ytb")) {
            email = principal.getName();
        } else {
            accountService.findByEmail(email);
        }
        emailService.mailChangePassword(email);
        return ResponseEntity.ok("emails will be sent in about 1 minutes, please check in your mail box");
    }

}
