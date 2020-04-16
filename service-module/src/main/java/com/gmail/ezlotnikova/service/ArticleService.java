package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import org.springframework.data.domain.Page;

public interface ArticleService {

    AddArticleDTO add(AddArticleDTO articleDTO);

    ArticleWithCommentsDTO findById(Long id);

    Page<ArticlePreviewDTO> findPaginatedAndOrderedByDate(int pageNumber, int pageSize);

    ExecutionResult deleteArticleById(Long id);

}