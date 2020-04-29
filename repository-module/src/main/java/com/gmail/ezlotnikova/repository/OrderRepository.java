package com.gmail.ezlotnikova.repository;

import java.util.Optional;

import com.gmail.ezlotnikova.repository.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends GenericRepository<Long, Order> {

    Page<Order> findPaginatedAndOrderedByDate(Pageable pageRequest);

    Page<Order> findPaginatedForUser(Pageable pageRequest, Long id);

}