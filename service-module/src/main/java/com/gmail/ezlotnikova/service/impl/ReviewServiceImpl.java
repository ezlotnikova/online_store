package com.gmail.ezlotnikova.service.impl;

import javax.transaction.Transactional;

import com.gmail.ezlotnikova.repository.ReviewRepository;
import com.gmail.ezlotnikova.repository.model.Review;
import com.gmail.ezlotnikova.service.ReviewService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.ShowReviewDTO;
import com.gmail.ezlotnikova.service.util.converter.ReviewConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.service.constant.PaginationConstant.REVIEWS_BY_PAGE;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public Page<ShowReviewDTO> findPaginated(int pageNumber) {
        /* page numeration in UI starts from 1, but in Pageable and Page objects it starts from zero,
        so parameter passed to PageRequest constructor is "pageNumber - 1" */
        Pageable pageRequest = PageRequest.of(pageNumber - 1, REVIEWS_BY_PAGE);
        return reviewRepository.findPaginated(pageRequest)
                .map(ReviewConverter::convertToReviewDTO);
    }

    @Override
    @Transactional
    public ExecutionResult deleteById(Long id) {
        Review review = reviewRepository.findById(id);
        if (review != null) {
            reviewRepository.remove(review);
            return ExecutionResult.ok();
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No review with id " + id + " found. ");
        }
    }

}