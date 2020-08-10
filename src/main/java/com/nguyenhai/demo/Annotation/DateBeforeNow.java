package com.nguyenhai.demo.Annotation;

import com.nguyenhai.demo.Annotation.Validator.DateBeforeNowValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateBeforeNowValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateBeforeNow {

    String message() default "invalid date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
