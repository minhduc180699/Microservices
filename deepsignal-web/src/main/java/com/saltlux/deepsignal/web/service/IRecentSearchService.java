package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.RecentSearch;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRecentSearchService {
    Page<RecentSearch> pagingByContentAndUserId(Pageable pageable, String userId, String content);

    List<RecentSearch> getAllByContentAndUserId(String userId);

    RecentSearch save(RecentSearch recentSearch);

    void deleteById(Long id);

    void deleteAllByUserId(String userId);
}
