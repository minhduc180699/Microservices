package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommentService {
    Comment save(Comment comment) throws Exception;

    Page<Comment> findByFeed(Pageable pageable, String feedId);

    long countCommentByFeed(String feedId);
}
