package com.gmail.ezlotnikova.web.controller.constant;

import com.gmail.ezlotnikova.repository.model.сonstant.OrderStatusEnum;
import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;

public interface ControllerTestConstant {

    String VALID_PAGE_NUMBER = "1";
    String INVALID_PAGE_NUMBER = "abc";
    String VALID_ID = "1";
    String INVALID_ID = "abc";

    String USER_LAST_NAME = "TestLastName";
    String USER_FIRST_NAME = "TestFirstName";
    String USER_PATRONYMIC_NAME = "TestPatronymicName";
    String USER_EMAIL = "test@test.test";
    UserRoleEnum USER_ROLE = UserRoleEnum.ADMINISTRATOR;
    String USER_TELEPHONE = "+375(11)111-11-11";

    OrderStatusEnum ORDER_STATUS = OrderStatusEnum.NEW;

    String ITEM_NAME = "TestItem";
    String ITEM_UNIQUE_NUMBER = "123e4567-e89b-12d3-a456-426655440000";
    String ITEM_DESCRIPTION = "TestDescription";
    int OBJECTS_BY_PAGE = 10;

    String VALID_ARTICLE_HEADER = "TestHeader";
    String VALID_ARTICLE_CONTENT_PREVIEW = "TestContentPreview";
    String VALID_ARTICLE_CONTENT = "TestArticleContent";
    String VALID_COMMENT_CONTENT = "TestCommentContent";
    String DATE = "2020-09-09 00:00:00";

}