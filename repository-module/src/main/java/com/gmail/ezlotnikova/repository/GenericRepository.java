package com.gmail.ezlotnikova.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericRepository<I, T> {

    T persist(T entity);

    void merge(T entity);

    void remove(T entity);

    T findById(I id);

    List<T> findAll();

    Page<T> findPaginated(Pageable pageRequest);

    Long getTotalCount();

}