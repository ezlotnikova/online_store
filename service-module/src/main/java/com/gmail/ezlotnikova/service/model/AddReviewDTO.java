package com.gmail.ezlotnikova.service.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_REVIEW_TEXT_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.NOT_EMPTY_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.TOO_LONG_REVIEW_TEXT;

public class AddReviewDTO {

    private Long id;
    private Long userId;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_REVIEW_TEXT_SIZE, message = TOO_LONG_REVIEW_TEXT)
    private String reviewText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddReviewDTO reviewDTO = (AddReviewDTO) o;
        return Objects.equals(id, reviewDTO.id) &&
                Objects.equals(userId, reviewDTO.userId) &&
                Objects.equals(reviewText, reviewDTO.reviewText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, reviewText);
    }

}