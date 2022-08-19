package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMemoService extends GeneralService<Memo, Long> {
    Page<Memo> findMemosByUserIdAndFeedId(String userId, String feedId, Pageable pageable);
}
