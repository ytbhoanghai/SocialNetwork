package com.nguyenhai.demo.Form;

import com.nguyenhai.demo.Annotation.Email;
import com.nguyenhai.demo.Annotation.Password;
import com.nguyenhai.demo.Annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    @Required(message = "\"firstName\" is not allowed to be blank")
    private String firstName;
    @Required(message = "\"lastName\" is not allowed to be blank")
    private String lastName;
    @Email
    private String email;
    @Password
    private String password;
    @NotNull(message = "\"acceptTermsAndConditions\" is not allowed to be null")
    @AssertTrue(message = "\"acceptTermsAndConditions\" must be accept")
    private Boolean acceptTermsAndConditions;

}
