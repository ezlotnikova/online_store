package com.gmail.ezlotnikova.repository.impl;

import javax.persistence.Query;

import com.gmail.ezlotnikova.repository.ArticleRepository;
import com.gmail.ezlotnikova.repository.model.Article;
import com.gmail.ezlotnikova.repository.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {

    private final PaginationUtil paginationUtil;

    public ArticleRepositoryImpl(PaginationUtil paginationUtil) {
        this.paginationUtil = paginationUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<Article> findPaginatedAndOrderedByDate(Pageable pageRequest) {
        int startPosition = paginationUtil.getQueryStartPosition(
                pageRequest.getPageNumber(), pageRequest.getPageSize());
        int maxResult = pageRequest.getPageSize();
        Long count = countTotal();
        String hql = "FROM Article as a ORDER BY a.createdOn";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return new PageImpl<Article>(query.getResultList(), pageRequest, count);
    }

}