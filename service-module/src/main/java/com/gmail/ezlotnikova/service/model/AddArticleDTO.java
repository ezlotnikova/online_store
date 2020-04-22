package com.gmail.ezlotnikova.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.EMAIL_ADDRESS_PATTERN;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_ARTICLE_CONTENT_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_ARTICLE_HEADER_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ValidationMessageConstant.INVALID_EMAIL_ADDRESS_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ValidationMessageConstant.NOT_EMPTY_MESSAGE;

public class AddArticleDTO {

    private Long id;
    @NotNull
    @Pattern(regexp = EMAIL_ADDRESS_PATTERN, message = INVALID_EMAIL_ADDRESS_MESSAGE)
    private String authorEmail;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_ARTICLE_HEADER_SIZE)
    private String header;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_ARTICLE_CONTENT_SIZE)
    private String content;
    private String createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}