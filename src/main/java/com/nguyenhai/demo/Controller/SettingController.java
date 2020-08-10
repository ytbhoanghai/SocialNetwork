package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Form.ContactInformationForm;
import com.nguyenhai.demo.Form.NewPasswordForm;
import com.nguyenhai.demo.Form.PersonalInformationForm;
import com.nguyenhai.demo.Response.ChangePasswordSuccessResponse;
import com.nguyenhai.demo.Response.UpdateImageSuccessResponse;
import com.nguyenhai.demo.Service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Base64;

@Controller
@RequestMapping(value = "/setting")
public class SettingController {

    private SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping
    public String requestPageSetting() { return "setting"; }

    @PostMapping(value = "avatar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAvatarProfile(@RequestParam String avatarBase64, Principal principal, @RequestParam(required = false, defaultValue = "false") Boolean createPost) throws IOException {
        String email = principal.getName();
        InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(avatarBase64));
        UpdateImageSuccessResponse response = settingService.updateAvatarProfile(inputStream, email, createPost);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "personal-information", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePersonalInformation(@Valid @RequestBody PersonalInformationForm personalInformationForm, Principal principal) {
        String email = principal.getName();
        settingService.updatePersonalInformation(personalInformationForm, email);
        return ResponseEntity.ok("update success personal information");
    }

    @PostMapping(value = "password", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestParam(required = false, defaultValue = "ytb") String email, @Valid @RequestBody NewPasswordForm newPasswordForm, Principal principal) {
        if (email.equals("ytb")) {
            email = principal.getName();
        }
        ChangePasswordSuccessResponse response = settingService.changePassword(newPasswordForm, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "contact-information", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateContactInformation(@Valid @RequestBody ContactInformationForm contactInformationForm, Principal principal) {
        String email = principal.getName();
        settingService.updateContactInformation(contactInformationForm, email);
        return ResponseEntity.ok("update contact information success");
    }
}
