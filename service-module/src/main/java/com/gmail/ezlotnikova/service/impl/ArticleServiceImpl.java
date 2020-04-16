package com.gmail.ezlotnikova.service.impl;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

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

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleConverter articleConverter;
    private final Validator validator;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository, ArticleConverter articleConverter, Validator validator) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleConverter = articleConverter;
        this.validator = validator;
    }

    @Override
    @Transactional
    public AddArticleDTO add(AddArticleDTO articleDTO) {
        Set<ConstraintViolation<AddArticleDTO>> violations = validator.validate(articleDTO);
        if (violations.isEmpty()) {
            User user = userRepository.loadUserByEmail(
                    articleDTO.getAuthorEmail());
            if (user != null) {
                Article article = articleConverter.convertToDatabaseObject(articleDTO);
                article.setUserDetails(
                        user.getUserDetails());
                Article addedArticle = articleRepository.persist(article);
                AddArticleDTO addedArticleDTO =  articleConverter.convertToAddArticleDTO(addedArticle);
                addedArticleDTO.setAuthorEmail(articleDTO.getAuthorEmail());
                return addedArticleDTO;
            } else {
                throw new IllegalArgumentException("No user found with specified email address");
            }
        } else {
            throw new IllegalArgumentException("Can't add article: invalid data provided");
        }
    }

    @Override
    @Transactional
    @Nullable
    public ArticleWithCommentsDTO findById(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            return articleConverter.convertToArticleWithCommentsDTO(article);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Page<ArticlePreviewDTO> findPaginatedAndOrderedByDate(int pageNumber, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleRepository.findPaginatedAndOrderedByDate(pageRequest)
                .map(articleConverter::convertToArticlePreviewDTO);
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