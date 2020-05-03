package com.gmail.ezlotnikova.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gmail.ezlotnikova.service.model.validation.TimestampMatches;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.DATE_PATTERN;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.EMAIL_ADDRESS_PATTERN;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_ARTICLE_CONTENT_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_ARTICLE_HEADER_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.INVALID_DATE_FORMAT;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.INVALID_EMAIL_ADDRESS_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.NOT_EMPTY_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.TOO_LONG_ARTICLE_CONTENT_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.TOO_LONG_ARTICLE_HEADER_MESSAGE;

@TimestampMatches
public class AddArticleDTO {

    private Long id;
    @Pattern(regexp = EMAIL_ADDRESS_PATTERN, message = INVALID_EMAIL_ADDRESS_MESSAGE)
    private String authorEmail;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_ARTICLE_HEADER_SIZE, message = TOO_LONG_ARTICLE_HEADER_MESSAGE)
    private String header;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_ARTICLE_CONTENT_SIZE, message = TOO_LONG_ARTICLE_CONTENT_MESSAGE)
    private String content;
    @Pattern(regexp = DATE_PATTERN, message = INVALID_DATE_FORMAT)
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}