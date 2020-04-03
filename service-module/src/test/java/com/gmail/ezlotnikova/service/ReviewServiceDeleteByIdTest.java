package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.repository.ReviewRepository;
import com.gmail.ezlotnikova.repository.model.Review;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.constant.ErrorCodeConstant;
import com.gmail.ezlotnikova.service.constant.ResultTypeEnum;
import com.gmail.ezlotnikova.service.constant.ServiceTestConstant;
import com.gmail.ezlotnikova.service.impl.ReviewServiceImpl;
import com.gmail.ezlotnikova.service.util.converter.ReviewConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceDeleteByIdTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewConverter reviewConverter;

    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        reviewService = new ReviewServiceImpl(
                reviewRepository, reviewConverter);
    }

    @Test
    public void deleteExistingReviewById_returnExecutedSuccessfully() {
        Long id = 1L;
        Review review = getReview();
        review.setId(id);
        when(reviewRepository.findById(id))
                .thenReturn(review);
        Assertions.assertThat(reviewService.deleteById(id).getResultType())
                .isEqualTo(ResultTypeEnum.EXECUTED_SUCCESSFULLY);
    }

    @Test
    public void deleteNonExistingReview_returnNoObjectFound() {
        Long id = 1L;
        Review review = getReview();
        review.setId(id);
        when(reviewRepository.findById(id))
                .thenReturn(null);
        Assertions.assertThat(reviewService.deleteById(id).getErrorCode())
                .isEqualTo(ErrorCodeConstant.NO_OBJECT_FOUND);
    }

    private Review getReview() {
        Review review = new Review();
        review.setReviewText(ServiceTestConstant.REVIEW_TEXT);
        review.setCreatedOn(ServiceTestConstant.TIMESTAMP);
        review.setVisible(true);
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(ServiceTestConstant.LAST_NAME);
        userDetails.setFirstName(ServiceTestConstant.FIRST_NAME);
        userDetails.setPatronymicName(ServiceTestConstant.PATRONYMIC_NAME);
        userDetails.setUser(new User());
        review.setUserDetails(userDetails);
        return review;
    }

}