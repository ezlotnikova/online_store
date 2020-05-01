package com.gmail.ezlotnikova.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_ARTICLE_CONTENT_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_ARTICLE_HEADER_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.NOT_EMPTY_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.TOO_LONG_ARTICLE_CONTENT_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.TOO_LONG_ARTICLE_HEADER_MESSAGE;

public class UpdateArticleDTO {

    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_ARTICLE_HEADER_SIZE, message = TOO_LONG_ARTICLE_HEADER_MESSAGE)
    private String newHeader;

    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_ARTICLE_CONTENT_SIZE, message = TOO_LONG_ARTICLE_CONTENT_MESSAGE)
    private String newContent;

    public String getNewHeader() {
        return newHeader;
    }

    public void setNewHeader(String newHeader) {
        this.newHeader = newHeader;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

}