package com.nguyenhai.demo.Annotation;

import com.nguyenhai.demo.Annotation.Validator.UrlValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UrlValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Url {

    String message() default "the url is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
