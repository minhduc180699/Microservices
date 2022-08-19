package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    Page<Memo> findMemosByUserIdAndFeedId(String userId, String feedId, Pageable pageable);
}
