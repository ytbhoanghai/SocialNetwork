package com.nguyenhai.demo.Annotation;

import com.nguyenhai.demo.Annotation.Validator.EmailValidator;
import com.nguyenhai.demo.Annotation.Validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "field password must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
