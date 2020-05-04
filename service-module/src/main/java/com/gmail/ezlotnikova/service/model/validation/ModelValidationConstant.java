package com.gmail.ezlotnikova.service.model.validation;

public interface ModelValidationConstant {

    int MAX_LAST_NAME_SIZE = 40;
    int MAX_FIRST_NAME_SIZE = 20;
    int MAX_PATRONYMIC_NAME_SIZE = 40;
    int MAX_ADDRESS_SIZE = 255;

    int MAX_ARTICLE_HEADER_SIZE = 100;
    int MAX_ARTICLE_CONTENT_SIZE = 1000;
    int MAX_COMMENT_SIZE = 200;

    int MAX_ITEM_NAME_SIZE = 100;
    int MAX_ITEM_DESCRIPTION_SIZE = 200;

    int MAX_REVIEW_TEXT_SIZE = 1000;

    String EMAIL_ADDRESS_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
    String PHONE_NUMBER_PATTERN = "\\+[0-9]{3}\\([0-9]{2}\\)[0-9]{3}-[0-9]{2}-[0-9]{2}|";
    String DATE_PATTERN = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}|";

}