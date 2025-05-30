package com.ead.authuser.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameConstraintImpl implements ConstraintValidator<UsernameContstraint, String> {
    @Override
    public void initialize(UsernameContstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {

        if(username == null || username.trim().isEmpty() || username.contains(" ")) {
            return false;
        }

        return true;
    }
}
