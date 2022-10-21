package com.saltlux.deepsignal.feedcache.api.client;

import com.saltlux.deepsignal.feedcache.dto.DataListResponse;
import com.saltlux.deepsignal.feedcache.dto.DataResponse;
import com.saltlux.deepsignal.feedcache.dto.FeedDto;
import com.saltlux.deepsignal.feedcache.model.FeedContentModel;
import com.saltlux.deepsignal.feedcache.model.FeedModel;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SearcherClientFallback implements SearcherClient{


    @Override
    public DataListResponse<FeedModel> getListFeed(String connectomeId, Integer page, Integer size) {
        return null;
    }

    @Override
    public DataListResponse<FeedModel> getListDocumentByIds(FeedDto feedDto) {
        return null;
    }

    @Override
    public DataListResponse<FeedContentModel> getListFeedContent(String feed_content_ids) {
        return null;
    }

    @Override
    public DataResponse<FeedContentModel> getFeedContent(String docId) {
        return null;
    }

    @Override
    public DataResponse<FeedModel> getFeed(String _id) {
        return null;
    }

    @Override
    public DataListResponse<FeedModel> searchFeed(String connectomeId, String keyword, String from, String until, Integer page, Integer size, String searchType, String channels, String lang, String type) {
        return null;
    }
}
