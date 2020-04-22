package com.gmail.ezlotnikova.repository;

import com.gmail.ezlotnikova.repository.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepository extends GenericRepository<Long, Article> {

    Page<Article> findPaginatedAndOrderedByDate(Pageable pageRequest);

}