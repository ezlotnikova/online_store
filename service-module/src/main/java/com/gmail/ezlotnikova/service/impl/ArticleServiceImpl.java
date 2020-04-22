package com.gmail.ezlotnikova.service.impl;

import com.gmail.ezlotnikova.repository.ArticleRepository;
import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.Article;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.util.converter.ArticleConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.service.constant.PaginationConstant.ARTICLES_BY_PAGE;
import static com.gmail.ezlotnikova.service.util.converter.ArticleConverter.convertToDatabaseObject;
import static com.gmail.ezlotnikova.service.util.converter.ArticleConverter.convertToAddArticleDTO;
import static com.gmail.ezlotnikova.service.util.converter.ArticleConverter.convertToArticleWithCommentsDTO;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            UserRepository userRepository
    ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AddArticleDTO add(AddArticleDTO articleDTO) {
        User user = userRepository.loadUserByEmail(
                articleDTO.getAuthorEmail());
        if (user != null) {
            Article article = convertToDatabaseObject(articleDTO);
            article.setUserDetails(
                    user.getUserDetails());
            Article addedArticle = articleRepository.persist(article);
            AddArticleDTO addedArticleDTO = convertToAddArticleDTO(addedArticle);
            addedArticleDTO.setAuthorEmail(articleDTO.getAuthorEmail());
            return addedArticleDTO;
        } else {
            throw new IllegalArgumentException("No user found with specified email address");
        }
    }

    @Override
    @Transactional
    @Nullable
    public ArticleWithCommentsDTO findById(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            return convertToArticleWithCommentsDTO(article);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Page<ArticlePreviewDTO> findPaginatedAndOrderedByDate(int pageNumber) {
        /* page numeration in UI starts from 1, but in Pageable and Page objects it starts from zero,
        so parameter passed to PageRequest constructor is "pageNumber - 1" */
        Pageable pageRequest = PageRequest.of(pageNumber - 1, ARTICLES_BY_PAGE);
        return articleRepository.findPaginatedAndOrderedByDate(pageRequest)
                .map(ArticleConverter::convertToArticlePreviewDTO);
    }

    @Override
    @Transactional
    public ExecutionResult deleteArticleById(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            articleRepository.remove(article);
            return ExecutionResult.ok();
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No article with id " + id + " found. ");
        }
    }

}