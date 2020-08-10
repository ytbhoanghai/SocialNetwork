package com.nguyenhai.demo.Annotation;

import com.nguyenhai.demo.Annotation.Validator.DateBeforeNowValidator;
import com.nguyenhai.demo.Annotation.Validator.SocialLinkValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SocialLinkValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SocialLink {

    String message() default "not enough value required (facebook, twitter, instagram, googlePlus, youtube, linkedin) or there is an invalid link";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
