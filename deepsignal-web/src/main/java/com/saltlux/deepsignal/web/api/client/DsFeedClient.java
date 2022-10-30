package com.saltlux.deepsignal.web.api.client;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Qualifier
@FeignClient(name = "deepsignal-feed-cache", fallback = DsFeedClientFallback.class)
public interface DsFeedClient {
    String API_CONNECTOME_FEED = "/goCache";

    @GetMapping(API_CONNECTOME_FEED + "/searchDoc")
    ResponseEntity<?> searchFeed(
        @RequestParam(value = "connectomeId", required = true) String connectomeId,
        @RequestParam(value = "requestId", required = false) String request_id,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "from", required = false) String from,
        @RequestParam(value = "until", required = false) String until,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
        @RequestParam(value = "search_type", required = false) String searchType,
        @RequestParam(value = "channels", required = false) String channels,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "lang", required = true, defaultValue = "en") String lang
    );

    @PostMapping(API_CONNECTOME_FEED + "/getListDocumentByIds")
    ResponseEntity<?> getListDocumentByIds(@RequestBody JSONObject bodyJSON);
}
