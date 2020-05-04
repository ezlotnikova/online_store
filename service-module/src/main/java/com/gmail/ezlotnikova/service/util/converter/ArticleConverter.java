package com.gmail.ezlotnikova.service.util.converter;

import java.sql.Timestamp;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.gmail.ezlotnikova.repository.model.Article;
import com.gmail.ezlotnikova.repository.model.Comment;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.model.CommentDTO;

import static com.gmail.ezlotnikova.service.util.DateTimeUtil.convertTimestampToString;
import static com.gmail.ezlotnikova.service.util.DateTimeUtil.getCurrentTimestamp;
import static com.gmail.ezlotnikova.service.util.converter.constant.ArticlePreviewConstant.ARTICLE_PREVIEW_LENGTH;

public class ArticleConverter {

    public static Article convertToDatabaseObject(AddArticleDTO articleDTO) {
        Article article = new Article();
        article.setHeader(
                articleDTO.getHeader());
        article.setContent(
                articleDTO.getContent());
        if (articleDTO.getDate() != null && !articleDTO.getDate().trim().isEmpty()) {
            article.setDate(Timestamp.valueOf(
                    articleDTO.getDate()));
        } else {
            article.setDate(
                    getCurrentTimestamp());
        }
        return article;
    }

    public static AddArticleDTO convertToAddArticleDTO(Article article) {
        AddArticleDTO articleDTO = new AddArticleDTO();
        articleDTO.setId(
                article.getId());
        articleDTO.setHeader(
                article.getHeader());
        articleDTO.setContent(
                article.getContent());
        articleDTO.setDate(convertTimestampToString(
                article.getDate()));
        return articleDTO;
    }

    public static ArticlePreviewDTO convertToArticlePreviewDTO(Article article) {
        ArticlePreviewDTO articlePreview = new ArticlePreviewDTO();
        articlePreview.setId(
                article.getId());
        articlePreview.setDate(convertTimestampToString(
                article.getDate()));
        articlePreview.setHeader(
                article.getHeader());
        UserDetails userDetails = article.getUserDetails();
        articlePreview.setAuthorFirstName(
                userDetails.getFirstName());
        articlePreview.setAuthorLastName(
                userDetails.getLastName());
        articlePreview.setContentPreview(getPreview(
                article.getContent()));
        return articlePreview;
    }

    public static ArticleWithCommentsDTO convertToArticleWithCommentsDTO(Article article) {
        ArticleWithCommentsDTO articleDTO = new ArticleWithCommentsDTO();
        articleDTO.setId(
                article.getId());
        articleDTO.setDate(convertTimestampToString(
                article.getDate()));
        articleDTO.setHeader(
                article.getHeader());
        UserDetails userDetails = article.getUserDetails();
        articleDTO.setAuthorFirstName(
                userDetails.getFirstName());
        articleDTO.setAuthorLastName(
                userDetails.getLastName());
        articleDTO.setContent(article.getContent());
        Set<Comment> comments = article.getComments();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        if (comments != null) {
            commentDTOList = comments.stream()
                    .map(ArticleConverter::convertToCommentDTO)
                    .collect(Collectors.toList());
        }
        articleDTO.setComments(commentDTOList);
        return articleDTO;
    }

    private static CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(
                comment.getId());
        UserDetails userDetails = comment.getUserDetails();
        commentDTO.setAuthorFirstName(
                userDetails.getFirstName());
        commentDTO.setAuthorLastName(
                userDetails.getLastName());
        commentDTO.setCreatedOn(convertTimestampToString(
                comment.getCreatedOn()));
        commentDTO.setContent(
                comment.getContent());
        return commentDTO;
    }

    private static String getPreview(String content) {
        if (content.length() <= ARTICLE_PREVIEW_LENGTH) {
            return content;
        } else {
            BreakIterator breakIterator = BreakIterator.getWordInstance();
            breakIterator.setText(content);
            return content.substring(0, breakIterator.preceding(ARTICLE_PREVIEW_LENGTH - 2)) + "...";
        }
    }

}