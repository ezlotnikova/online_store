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

    @Override
    @SuppressWarnings("unchecked")
    public Page<Order> findPaginatedForUser(Pageable pageRequest, Long userId) {
        int startPosition = getQueryStartPosition(
                pageRequest.getPageNumber(), pageRequest.getPageSize());
        int maxResult = pageRequest.getPageSize();
        Long count = getCountForUser(userId);
        String hql = "FROM Order as O WHERE O.userDetails.userId =:userId ORDER BY O.createdOn DESC ";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        query.setParameter("userId", userId);
        return new PageImpl<Order>(query.getResultList(), pageRequest, count);
    }

    private Long getCountForUser(Long userId) {
        String hql = "SELECT COUNT(O.id) FROM Order as O WHERE O.userDetails.userId =:userId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        return (Long) query.getSingleResult();
    }

}