package com.gmail.ezlotnikova.service.model.validation;

public interface ModelValidationMessageConstant {

    String NOT_EMPTY_MESSAGE = "Is required";
    String INVALID_EMAIL_ADDRESS_MESSAGE = "Invalid email address";
    String INVALID_PHONE_NUMBER_MESSAGE = "Invalid telephone number";
    String TOO_LONG_ADDRESS_MESSAGE = "Address must be no longer than 250 characters";
    String PRICE_LESS_THEN_ZERO_MESSAGE = "Price must be greater then 0";
    String TOO_LONG_ITEM_NAME = "Item name must be no longer than 100 characters";
    String TOO_LONG_ITEM_DESCRIPTION = "Item description must be no longer than 200 characters";
    String TOO_LONG_ARTICLE_HEADER_MESSAGE = "Header must be no longer than 100 characters";
    String TOO_LONG_ARTICLE_CONTENT_MESSAGE = "Article must be no longer than 1000 characters";
    String TOO_LONG_COMMENT_TEXT = "Comment must be no longer then 200 characters";
    String INVALID_DATE_FORMAT = "Please use format shown below";
    String TOO_LONG_REVIEW_TEXT = "Review must be no longer then 1000 characters";

}