package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.AddCommentDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import org.springframework.data.domain.Page;

public interface ArticleService {

    AddArticleDTO add(AddArticleDTO articleDTO);

    AddArticleDTO findArticleById(Long id);

    ArticleWithCommentsDTO findArticleWithCommentsById(Long id);

    ExecutionResult saveChanges(AddArticleDTO articleDTO);

    Page<ArticlePreviewDTO> findPaginatedAndOrderedByDate(int pageNumber);

    ExecutionResult deleteArticleById(Long id);

    ExecutionResult addComment(AddCommentDTO comment);

    ExecutionResult deleteComment(Long commentId);

}