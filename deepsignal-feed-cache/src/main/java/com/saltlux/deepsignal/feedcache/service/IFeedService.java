package com.saltlux.deepsignal.feedcache.service;

import com.saltlux.deepsignal.feedcache.dto.response.DataListResponse;
import com.saltlux.deepsignal.feedcache.dto.response.DataResponse;
import com.saltlux.deepsignal.feedcache.dto.response.ResponseDocument;
import com.saltlux.deepsignal.feedcache.dto.response.ResultResponse;
import com.saltlux.deepsignal.feedcache.model.*;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetDocument;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetListDoc;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IFeedService {
    public DataListResponse<DocModel> getListFeed(String connectomeId, String request_id, Integer page, Integer size);

    public DataListResponse<?> getListDocumentByIds(RequestBodyGetListDoc requestBody);

    public DataListResponse<DocModel> searchFeed(String connectomeId, String request_id, String keyword, String from, String until, Integer page, Integer size, String searchType, String channels, String lang, String type, String sortBy, Float score, List<String> writer);

    public DataListResponse<DocModel> getListFilterFeed(String connectomeId, String request_id, Integer page, Integer size, String type);

    public DataResponse<DocDataModel> getFeed(String connectomeId, String request_id, String feed_id);

    public ResultResponse createFeed(DocCreateModel data);

    public ResultResponse updateFeed(FeedUpdateModel feedModel);

    public ResultResponse likeFeed(FeedInputModel feedModel);

    public ResultResponse hideFeed(FeedInputModel feedModel);

    public ResultResponse bookmarkFeed(FeedInputModel feedModel);

    public ResponseDocument getDocumentById(RequestBodyGetDocument requestBody);
}
