package com.saltlux.deepsignal.adapter.service;

import com.saltlux.deepsignal.adapter.domain.ConnectomeFeed;
import com.saltlux.deepsignal.adapter.domain.Feed;
import com.saltlux.deepsignal.adapter.service.dto.FilterFeedDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IConnectomeFeedService {
    Page<ConnectomeFeed> findAll(int page, int size, String orderBy, String sortDirection);

    Page<ConnectomeFeed> findByConnectomId(int page, int size, String orderBy, String sortDirection, String connectomeId);

    Page<Feed> findFeedByConnectomeId(int page, int size, String orderBy, String sortDirection, String connectomeId);

    Page<Feed> findByConnectomeIdAndKeyword(int page, int size, String orderBy, String sortDirection, String connectomeId, String keyword);

    Page<Feed> findFeedByConnectomeIdAndInteraction(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String interaction,
        String interactionValue
    );

    Page<Feed> findFeedByConnectomeIdKeywordAndInteraction(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String keyword,
        String interaction,
        String interactionValue
    );

    Page<Feed> findFeedByConnectomeIdKeywordAndFilter(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String keyword,
        List<FilterFeedDTO> filterFeedDTOS
    );

    Page<Feed> findFeedByConnectomIdAndTopic(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String topic,
        boolean excepted
    );

    Optional<ConnectomeFeed> findById(String id);

    long countAllFeedByConnectomeId(String connectomeId);

    boolean handleShare(String id, String platform);

    boolean handleActivity(String id, boolean state, String activity, String connectomeId, String page, int likeState);

    Optional<Feed> findDetailCardById(String id);

    boolean handleMemo(String feedId, int status);

    List<Feed> findFeedByConnectomeIdAndLangAndRecommendDate(String connectomeId, String lang, Instant datetime);
    long countFeedByConnectomeIdAndRecommendDate(String connectomeId, Instant datetime);

    Page<Feed> findFeedByConnectomeIdAndActivity(
        String connectomeId,
        FilterFeedDTO filterFeedDTO,
        String lang,
        int page,
        int size,
        String orderBy,
        String sortDirection
    );

    List<Feed> findFeedByIds(List<String> ids, Boolean... isDeleted);
}
