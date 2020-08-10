package com.nguyenhai.demo.Form;

import com.nguyenhai.demo.Annotation.Password;
import com.nguyenhai.demo.Annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordForm {

    @Password
    private String newPassword;
    @Required
    private String verificationCode;

}
