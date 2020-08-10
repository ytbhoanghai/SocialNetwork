package com.nguyenhai.demo.Annotation.Validator;

import com.nguyenhai.demo.Annotation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.isEmpty() || value.matches("^$|[0-9]{6,10}");
    }
}
