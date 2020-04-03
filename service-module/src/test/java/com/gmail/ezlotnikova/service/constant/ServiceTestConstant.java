package com.gmail.ezlotnikova.service.constant;

import java.sql.Timestamp;

import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;

public interface ServiceTestConstant {

    String LAST_NAME = "TestLastName";
    String FIRST_NAME = "TestFirstName";
    String PATRONYMIC_NAME = "TestPatronymicName";
    String EMAIL = "test@test.test";
    UserRoleEnum ROLE = UserRoleEnum.ADMINISTRATOR;
    String REVIEW_TEXT = "TestReviewText";
    Timestamp TIMESTAMP = Timestamp.valueOf("1984-09-09 00:00:00");

}