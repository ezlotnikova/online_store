package com.gmail.ezlotnikova.repository.impl;

import javax.persistence.Query;

import com.gmail.ezlotnikova.repository.OrderRepository;
import com.gmail.ezlotnikova.repository.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.gmail.ezlotnikova.repository.util.PaginationUtil.getQueryStartPosition;

@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    @SuppressWarnings("unchecked")
    public Page<Order> findPaginatedAndOrderedByDate(Pageable pageRequest) {
        int startPosition = getQueryStartPosition(
                pageRequest.getPageNumber(), pageRequest.getPageSize());
        int maxResult = pageRequest.getPageSize();
        Long count = getTotalCount();
        String hql = "FROM Order as O ORDER BY O.createdOn DESC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return new PageImpl<Order>(query.getResultList(), pageRequest, count);
    }

}