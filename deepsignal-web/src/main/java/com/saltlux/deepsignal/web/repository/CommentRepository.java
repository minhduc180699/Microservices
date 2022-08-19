package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    Page<Comment> findByFeedIdAndCommentIsNull(Pageable pageable, String feedId);

    long countAllByFeedId(String feedId);
}
