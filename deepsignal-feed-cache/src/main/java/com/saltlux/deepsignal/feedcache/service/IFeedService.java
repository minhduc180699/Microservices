package com.saltlux.deepsignal.feedcache.service;

import com.saltlux.deepsignal.feedcache.dto.*;
import com.saltlux.deepsignal.feedcache.model.FeedDataModel;
import com.saltlux.deepsignal.feedcache.model.FeedModel;
import org.springframework.stereotype.Service;

@Service
public interface IFeedService {
    public DataListResponse<FeedModel> getListFeed(String connectomeId, String request_id, Integer page, Integer size);
    public DataListResponse<FeedModel> getListDocumentByIds(FeedIdsDto feedDto);
    public DataListResponse<FeedModel> searchFeed(String connectomeId, String request_id, String keyword, String from, String until, Integer page, Integer size, String searchType, String channels, String lang, String type);
    public DataListResponse<FeedModel> getListFilterFeed(String connectomeId, String request_id, Integer page, Integer size, String type);
    public DataResponse<FeedDataModel> getFeed(String connectomeId, String request_id, String feed_id);
    public ResultResponse createFeed(FeedDataModel data);
    public ResultResponse updateFeed(FeedModel feedModel);
    public ResultResponse likeFeed(FeedModel feedModel);
    public ResultResponse hideFeed(FeedModel feedModel);

    public ResultResponse bookmarkFeed(FeedModel feedModel);
}
