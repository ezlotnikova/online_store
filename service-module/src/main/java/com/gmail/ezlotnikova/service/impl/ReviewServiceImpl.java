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

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewConverter reviewConverter) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
    }

    @Override
    @Transactional
    public Page<ShowReviewDTO> findPaginated(int pageNumber, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        return reviewRepository.findPaginated(pageRequest)
                .map(reviewConverter::convertDatabaseObjectToDTO);
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