package com.nguyenhai.demo.Annotation.Validator;

import com.nguyenhai.demo.Annotation.Required;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredValidator implements ConstraintValidator<Required, String> {

   @Override
   public boolean isValid(String value, ConstraintValidatorContext context)  {
      return value != null && !value.isEmpty() && !value.trim().isEmpty();
   }
}
