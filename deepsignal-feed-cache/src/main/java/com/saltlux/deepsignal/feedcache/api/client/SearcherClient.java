package com.saltlux.deepsignal.feedcache.api.client;

import com.saltlux.deepsignal.feedcache.dto.DataListResponse;
import com.saltlux.deepsignal.feedcache.dto.DataResponse;
import com.saltlux.deepsignal.feedcache.dto.FeedDto;
import com.saltlux.deepsignal.feedcache.model.FeedContentModel;
import com.saltlux.deepsignal.feedcache.model.FeedModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Component
@FeignClient(name = "deepsignal-searcher", fallback = SearcherClientFallback.class)
public interface SearcherClient {
    String API_SEARCHER = "/feedApi";

    @GetMapping(API_SEARCHER + "/getListFeed")
    DataListResponse<FeedModel> getListFeed(@RequestParam("connectomeId") String connectomeId,
                                 @RequestParam("page") Integer page,
                                 @RequestParam("size") Integer size);
    @PostMapping(API_SEARCHER + "/getListDocumentByIds")
    DataListResponse<FeedModel> getListDocumentByIds(@RequestBody FeedDto feedDto);
    @GetMapping(API_SEARCHER + "/getListFeedContent")
    DataListResponse<FeedContentModel> getListFeedContent(@RequestParam String feed_content_ids);
    @GetMapping(API_SEARCHER + "/getFeedContent")
    DataResponse<FeedContentModel> getFeedContent(@RequestParam String docId);
    @GetMapping(API_SEARCHER + "/getFeed")
    DataResponse<FeedModel> getFeed(@RequestParam("_id") String _id);
    @GetMapping(API_SEARCHER + "/searchFeedData")
    DataListResponse<FeedModel> searchFeed(@RequestParam("connectomeId") String connectomeId,
                               @RequestParam("keyword") String keyword,
                               @RequestParam("from") String from,
                               @RequestParam("until") String until,
                               @RequestParam("page") Integer page,
                               @RequestParam("size") Integer size,
                               @RequestParam("searchType") String searchType,
                               @RequestParam("channels") String channels,
                               @RequestParam("lang") String lang,
                               @RequestParam("type") String type);
}
