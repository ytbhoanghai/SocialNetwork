package com.nguyenhai.demo.Annotation;

import com.nguyenhai.demo.Annotation.Validator.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "invalid phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
