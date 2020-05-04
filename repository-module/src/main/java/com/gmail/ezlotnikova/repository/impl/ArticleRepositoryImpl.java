package com.gmail.ezlotnikova.repository.impl;

import java.sql.Timestamp;
import java.util.Date;
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
        Timestamp now = getCurrentTimestamp();
        String hql = "FROM Article as A WHERE A.date <= :now ORDER BY A.date DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("now", now);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return new PageImpl<Article>(query.getResultList(), pageRequest, count);
    }

    private Timestamp getCurrentTimestamp() {
        Date date = new Date();
        long time = date.getTime();
        return new Timestamp(time);
    }

}