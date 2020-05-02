package com.gmail.ezlotnikova.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_COMMENT_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.NOT_EMPTY_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.TOO_LONG_COMMENT_TEXT;

public class AddCommentDTO {

    private Long id;
    private Long articleId;
    private Long userId;
    public String createdOn;

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_COMMENT_SIZE, message = TOO_LONG_COMMENT_TEXT)
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}