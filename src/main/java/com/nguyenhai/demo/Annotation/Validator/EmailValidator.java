package com.nguyenhai.demo.Annotation.Validator;

import com.nguyenhai.demo.Annotation.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
}
