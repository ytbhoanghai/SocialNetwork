package com.nguyenhai.demo.Annotation.Validator;

import com.nguyenhai.demo.Annotation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}");
    }
}
