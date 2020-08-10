package com.nguyenhai.demo.Annotation.Validator;

import com.nguyenhai.demo.Annotation.DateBeforeNow;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class DateBeforeNowValidator implements ConstraintValidator<DateBeforeNow, Date> {

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        return value.before(new Date());
    }
}
