package com.saltlux.deepsignal.feedcache.api.client;

import com.saltlux.deepsignal.feedcache.dto.response.DataListResponse;
import com.saltlux.deepsignal.feedcache.dto.response.DataResponse;
import com.saltlux.deepsignal.feedcache.dto.response.ResponseDocument;
import com.saltlux.deepsignal.feedcache.model.DocContentModel;
import com.saltlux.deepsignal.feedcache.model.DocModel;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetDocument;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetListDoc;
import org.springframework.stereotype.Component;

@Component
public class SearcherClientFallback implements SearcherClient{


    @Override
    public DataListResponse<DocModel> getListFeed(String connectomeId, Integer page, Integer size) {
        return null;
    }

    @Override
    public DataListResponse<?> getListDocumentByIds(RequestBodyGetListDoc requestBody) {
        return null;
    }

    @Override
    public DataListResponse<DocContentModel> getListFeedContent(String feed_content_ids) {
        return null;
    }

    @Override
    public DataResponse<DocContentModel> getFeedContent(String docId) {
        return null;
    }

    @Override
    public DataResponse<DocModel> getFeed(String connectomeId, String docId) {
        return null;
    }

    @Override
    public DataListResponse<DocModel> searchFeed(String connectomeId, String keyword, String from, String until, Integer page, Integer size, String searchType, String channels, String lang, String type) {
        return null;
    }

    @Override
    public DataListResponse<DocModel> getListFilterFeed(String connectomeId, Integer page, Integer size, String type) {
        return null;
    }

    @Override
    public ResponseDocument getDocumentById(RequestBodyGetDocument requestBody) {
        return null;
    }
}
