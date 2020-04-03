package com.gmail.ezlotnikova.repository.impl;

import com.gmail.ezlotnikova.repository.ReviewRepository;
import com.gmail.ezlotnikova.repository.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {

}