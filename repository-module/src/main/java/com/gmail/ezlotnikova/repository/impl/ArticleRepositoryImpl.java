package com.gmail.ezlotnikova.repository.impl;

import javax.persistence.Query;

import com.gmail.ezlotnikova.repository.ArticleRepository;
import com.gmail.ezlotnikova.repository.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.gmail.ezlotnikova.repository.util.PaginationUtil.getQueryStartPosition;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {

    @Override
    @SuppressWarnings("unchecked")
    public Page<Article> findPaginatedAndOrderedByDate(Pageable pageRequest) {
        int startPosition = getQueryStartPosition(
                pageRequest.getPageNumber(), pageRequest.getPageSize());
        int maxResult = pageRequest.getPageSize();
        Long count = getTotalCount();
        String hql = "FROM Article as A ORDER BY A.date DESC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return new PageImpl<Article>(query.getResultList(), pageRequest, count);
    }

}