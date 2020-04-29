package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddReviewDTO;
import com.gmail.ezlotnikova.service.model.ShowReviewDTO;
import org.springframework.data.domain.Page;

public interface ReviewService {

    Page<ShowReviewDTO> findPaginated(int pageNumber);

    ExecutionResult deleteById(Long id);

    AddReviewDTO add(AddReviewDTO review);

}