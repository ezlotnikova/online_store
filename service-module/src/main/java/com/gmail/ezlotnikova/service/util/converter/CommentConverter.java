package com.gmail.ezlotnikova.service.util.converter;

import com.gmail.ezlotnikova.repository.model.Comment;
import com.gmail.ezlotnikova.service.model.AddCommentDTO;
import com.gmail.ezlotnikova.service.util.DateTimeUtil;

public class CommentConverter {

    public static Comment convertToDatabaseObject(AddCommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setArticleId(
                commentDTO.getArticleId());
        comment.setContent(
                commentDTO.getContent());
        return comment;
    }

    public static AddCommentDTO convertToAddCommentDTO(Comment comment) {
        AddCommentDTO commentDTO = new AddCommentDTO();
        commentDTO.setId(
                comment.getId());
        commentDTO.setArticleId(
                comment.getArticleId());
        commentDTO.setCreatedOn(DateTimeUtil.convertTimestampToString(
                comment.getCreatedOn()));
        commentDTO.setContent(
                comment.getContent());
        commentDTO.setUserId(
                comment.getUserDetails()
                        .getUserId());
        return commentDTO;
    }

}