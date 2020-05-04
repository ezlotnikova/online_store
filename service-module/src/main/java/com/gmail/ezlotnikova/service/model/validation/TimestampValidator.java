package com.gmail.ezlotnikova.service.model.validation;

import java.sql.Timestamp;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.util.DateTimeUtil;

public class TimestampValidator implements ConstraintValidator<TimestampMatches, Object> {

    @Override
    public void initialize(TimestampMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        AddArticleDTO addArticleDTO = (AddArticleDTO) obj;
        return (addArticleDTO.getDate() == null || addArticleDTO.getDate().trim().isEmpty()
                || Timestamp.valueOf(addArticleDTO.getDate()).compareTo(DateTimeUtil.getCurrentTimestamp()) >= 0);
    }

}