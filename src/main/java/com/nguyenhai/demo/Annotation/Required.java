package com.nguyenhai.demo.Annotation;

import com.nguyenhai.demo.Annotation.Validator.RequiredValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RequiredValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {

    String message() default "invalid input";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
