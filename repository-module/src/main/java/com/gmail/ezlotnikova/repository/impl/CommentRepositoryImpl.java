package com.gmail.ezlotnikova.repository.impl;

import com.gmail.ezlotnikova.repository.CommentRepository;
import com.gmail.ezlotnikova.repository.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {

}