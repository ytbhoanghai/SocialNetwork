package com.nguyenhai.demo.Annotation;

import com.nguyenhai.demo.Annotation.Validator.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "invalid email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
