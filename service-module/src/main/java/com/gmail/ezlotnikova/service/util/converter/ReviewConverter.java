package com.gmail.ezlotnikova.service.util.converter;

import com.gmail.ezlotnikova.repository.model.Review;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.model.AddReviewDTO;
import com.gmail.ezlotnikova.service.model.ShowReviewDTO;

import static com.gmail.ezlotnikova.service.util.converter.DateTimeUtil.convertTimestampToString;

public class ReviewConverter {

    public static Review convertToDatabaseObject(AddReviewDTO reviewDTO) {
        Review review = new Review();
        review.setReviewText(
                reviewDTO.getReviewText());
        return review;
    }

    public static AddReviewDTO convertToAddReviewDTO(Review review) {
        AddReviewDTO reviewDTO = new AddReviewDTO();
        reviewDTO.setId(
                review.getId());
        UserDetails userDetails = review.getUserDetails();
        reviewDTO.setUserId(
                userDetails.getUserId());
        reviewDTO.setReviewText(
                review.getReviewText());
        return reviewDTO;
    }

    public static ShowReviewDTO convertToReviewDTO(Review review) {
        ShowReviewDTO reviewDTO = new ShowReviewDTO();
        reviewDTO.setId(
                review.getId());
        UserDetails userDetails = review.
                getUserDetails();
        reviewDTO.setLastName(
                userDetails.getLastName());
        reviewDTO.setFirstName(
                userDetails.getLastName());
        reviewDTO.setPatronymicName(
                userDetails.getPatronymicName());
        reviewDTO.setReviewText(
                review.getReviewText());
        reviewDTO.setCreatedOn(convertTimestampToString(
                review.getCreatedOn()));
        reviewDTO.setVisible(
                review.getVisible());
        return reviewDTO;
    }

}