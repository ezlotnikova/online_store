package com.gmail.ezlotnikova.service.impl;

import com.gmail.ezlotnikova.repository.ArticleRepository;
import com.gmail.ezlotnikova.repository.CommentRepository;
import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.Article;
import com.gmail.ezlotnikova.repository.model.Comment;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.AddCommentDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.util.DateTimeUtil;
import com.gmail.ezlotnikova.service.util.converter.ArticleConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.FAILED_TO_EXECUTE;
import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.service.constant.PaginationConstant.ARTICLES_BY_PAGE;
import static com.gmail.ezlotnikova.service.util.converter.ArticleConverter.convertToAddArticleDTO;
import static com.gmail.ezlotnikova.service.util.converter.ArticleConverter.convertToArticleWithCommentsDTO;
import static com.gmail.ezlotnikova.service.util.converter.ArticleConverter.convertToDatabaseObject;
import static com.gmail.ezlotnikova.service.util.converter.CommentConverter.convertToAddCommentDTO;
import static com.gmail.ezlotnikova.service.util.converter.CommentConverter.convertToDatabaseObject;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
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
            addedArticleDTO.setAuthorEmail(user.getEmail());
            return addedArticleDTO;
        } else {
            throw new IllegalArgumentException("No user found with specified email address");
        }
    }

    @Override
    @Transactional
    @Nullable
    public AddArticleDTO findArticleById(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            return convertToAddArticleDTO(article);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    @Nullable
    public ArticleWithCommentsDTO findArticleWithCommentsById(Long id) {
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
    public ExecutionResult saveChanges(AddArticleDTO articleDTO) {
        Long id = articleDTO.getId();
        Article article = articleRepository.findById(id);
        if (article != null) {
            article.setHeader(
                    articleDTO.getHeader());
            article.setContent(
                    articleDTO.getContent());
            article.setDate(DateTimeUtil.getCurrentTimestamp());
            articleRepository.merge(article);
            return ExecutionResult.ok();
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No article with id " + id + " found. ");
        }
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

    @Override
    @Transactional
    public ExecutionResult addComment(AddCommentDTO commentDTO) {
        Article article = articleRepository.findById(commentDTO.getArticleId());
        if (article != null) {
            User user = userRepository.findById(
                    commentDTO.getUserId());
            if (user != null) {
                Comment comment = convertToDatabaseObject(commentDTO);
                comment.setUserDetails(
                        user.getUserDetails());
                comment.setCreatedOn(DateTimeUtil.getCurrentTimestamp());
                Comment addedComment = commentRepository.persist(comment);
                AddCommentDTO addedCommentDTO = convertToAddCommentDTO(addedComment);
                if (addedCommentDTO.getId() != null) {
                    return ExecutionResult.ok();
                } else {
                    return ExecutionResult.error(FAILED_TO_EXECUTE, "Saving comment failed. Please try again.");
                }
            } else {
                return ExecutionResult.error(NO_OBJECT_FOUND, "No user found.");
            }
        }
        return ExecutionResult.error(NO_OBJECT_FOUND, "No article found.");
    }

    @Override
    @Transactional
    public ExecutionResult deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId);
        if (comment != null) {
            commentRepository.remove(comment);
            return ExecutionResult.ok();
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No comment with id " + commentId + " found. ");
        }

    }

}