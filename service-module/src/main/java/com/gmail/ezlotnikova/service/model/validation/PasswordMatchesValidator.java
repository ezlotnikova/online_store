package com.gmail.ezlotnikova.service.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.gmail.ezlotnikova.service.model.PasswordDTO;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        PasswordDTO passwordDTO = (PasswordDTO) obj;
        return passwordDTO.getNewPassword().equals(passwordDTO.getMatchingPassword());
    }

}