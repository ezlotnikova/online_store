package com.gmail.ezlotnikova.service.util.converter;

import com.gmail.ezlotnikova.repository.model.Review;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.model.ShowReviewDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

    private final DateTimeConverter dateTimeConverter;

    public ReviewConverter(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    public ShowReviewDTO convertDatabaseObjectToDTO(Review review) {
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
        reviewDTO.setCreatedOn(dateTimeConverter.convertTimestampToString(
                review.getCreatedOn()));
        reviewDTO.setVisible(
                review.getVisible());
        return reviewDTO;
    }

}