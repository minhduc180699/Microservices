package com.saltlux.deepsignal.feedcache.api.client;

import com.saltlux.deepsignal.feedcache.dto.response.DataListResponse;
import com.saltlux.deepsignal.feedcache.dto.response.DataResponse;
import com.saltlux.deepsignal.feedcache.dto.response.ResponseDocument;
import com.saltlux.deepsignal.feedcache.model.DocContentModel;
import com.saltlux.deepsignal.feedcache.model.DocModel;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetDocument;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetListDoc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "deepsignal-searcher", fallback = SearcherClientFallback.class)
public interface SearcherClient {
    String API_SEARCHER = "/goSearcher";

    @GetMapping(API_SEARCHER + "/getListDocument")
    DataListResponse<DocModel> getListFeed(@RequestParam("connectomeId") String connectomeId,
                                           @RequestParam("page") Integer page,
                                           @RequestParam("size") Integer size);

    @PostMapping(API_SEARCHER + "/getListDocumentByIds")
    DataListResponse<?> getListDocumentByIds(@RequestBody RequestBodyGetListDoc requestBody);

    @GetMapping(API_SEARCHER + "/getListDocumentContent")
    DataListResponse<DocContentModel> getListFeedContent(@RequestParam String feed_content_ids);

    @GetMapping(API_SEARCHER + "/getDocumentContent")
    DataResponse<DocContentModel> getFeedContent(@RequestParam String docId);

    @GetMapping(API_SEARCHER + "/getDocument")
    DataResponse<DocModel> getFeed(@RequestParam("connectomeId") String connectomeId,
                                   @RequestParam("docId") String docId);

    @GetMapping(API_SEARCHER + "/searchDocumentData")
    DataListResponse<DocModel> searchFeed(@RequestParam("connectomeId") String connectomeId,
                                          @RequestParam("keyword") String keyword,
                                          @RequestParam("from") String from,
                                          @RequestParam("until") String until,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size,
                                          @RequestParam("search_type") String searchType,
                                          @RequestParam("channels") String channels,
                                          @RequestParam("lang") String lang,
                                          @RequestParam("type") String type);


    @GetMapping(API_SEARCHER + "/getListFilterDocument")
    DataListResponse<DocModel> getListFilterFeed(@RequestParam("connectomeId") String connectomeId,
                                                 @RequestParam("page") Integer page,
                                                 @RequestParam("size") Integer size,
                                                 @RequestParam("type") String type);

    @PostMapping(API_SEARCHER + "/getDocumentById")
    ResponseDocument getDocumentById(@RequestBody RequestBodyGetDocument requestBody);
}
