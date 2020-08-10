package com.nguyenhai.demo.Annotation.Validator;

import com.nguyenhai.demo.Annotation.Url;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<Url, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.isEmpty() || value.matches("^$|^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");
    }
}
