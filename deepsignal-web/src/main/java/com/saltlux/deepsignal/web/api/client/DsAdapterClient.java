package com.saltlux.deepsignal.web.api.client;

import com.saltlux.deepsignal.web.service.dto.ConnectomeFeed;
import com.saltlux.deepsignal.web.service.dto.FilterFeedDTO;
import com.saltlux.deepsignal.web.service.dto.MetaSearchDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Qualifier
@FeignClient(name = "deepsignal-adapter", fallback = DsAdapterClientFallback.class)
public interface DsAdapterClient {
    String API_CONNECTOME_FEED = "/api/connectome-feed/";

    /************************************************ Connecto Feed ************************************************************/
    @GetMapping(API_CONNECTOME_FEED + "getAll")
    ResponseEntity<Map<String, Object>> getAllConnectomeFeed(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "score") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    );

    @GetMapping(API_CONNECTOME_FEED + "{id}")
    @Operation(summary = "Get connectome feed by Id", tags = { "Connectome Feed Management" })
    ResponseEntity<ConnectomeFeed> getConnetomeFeedById(@PathVariable(value = "id") String id);

    @PostMapping(API_CONNECTOME_FEED + "getByIds")
    ResponseEntity<?> getByIds(@RequestBody List<String> ids);

    @PostMapping(API_CONNECTOME_FEED + "getListFeeds/{connectomeId}")
    ResponseEntity<Map<String, Object>> getFeedByConnectomeId(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "recommendDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "keyword", required = false) String keyword,
        @PathVariable("connectomeId") String connectomeId,
        @RequestBody List<FilterFeedDTO> filterFeedDTOS
    );

    @GetMapping(API_CONNECTOME_FEED + "countByConnectomeId/{connectomeId}")
    ResponseEntity<Map<String, Object>> countFeedByConnectomeId(@PathVariable("connectomeId") String connectomeId);

    @GetMapping(API_CONNECTOME_FEED + "getListFeeds/{connectomeId}/{topic}")
    @Operation(summary = "Get feed by connectome Id and topic", tags = { "Connectome Feed Management" })
    ResponseEntity<Map<String, Object>> getFeedByConnectomeIdAndTopic(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "timestamp") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @PathVariable("connectomeId") String connectomeId,
        @PathVariable("topic") String topic,
        @RequestParam("excepted") boolean excepted
    );

    @GetMapping(API_CONNECTOME_FEED + "handleActivity")
    ResponseEntity<?> handleActivity(
        @RequestParam("docId") String docId,
        @RequestParam("state") boolean state,
        @RequestParam("activity") String activity,
        @RequestParam("connectomeId") String connectomeId,
        @RequestParam("page") String page,
        @RequestParam("likeState") int isLiked
    );

    @GetMapping(API_CONNECTOME_FEED + "sharingCard")
    ResponseEntity<?> handleSharing(@RequestParam("id") String id, @RequestParam("platform") String platform);

    @GetMapping(API_CONNECTOME_FEED + "getDetailCard")
    ResponseEntity<?> getDetailCard(@RequestParam("id") String id);

    @PostMapping(API_CONNECTOME_FEED + "memo")
    ResponseEntity<?> handleMemo(@RequestParam("feedId") String feedId, @RequestParam("status") Integer status);

    @GetMapping(API_CONNECTOME_FEED + "getFeedByDate/{connectomeId}")
    ResponseEntity<?> getByConnectomeIdAndDate(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("lang") String lang,
        @RequestParam("recommendDate") Instant recommendDate
    );

    @GetMapping(API_CONNECTOME_FEED + "countFeedByDate/{connectomeId}")
    ResponseEntity<Long> countFeedByDate(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("recommendDate") Instant recommendDate
    );

    @PostMapping(API_CONNECTOME_FEED + "getActivity/{connectomeId}")
    ResponseEntity<?> getActivity(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("orderBy") String orderBy,
        @RequestParam("sortDirection") String sortDirection,
        @RequestParam(value = "lang", required = false) String lang,
        @RequestBody List<FilterFeedDTO> filterFeedDTOS
    );

    /************************************************** Peoples/Company ************************************************************/
    @GetMapping("/getAll")
    @Operation(summary = "Get all peoples", tags = { "Peoples Management" })
    ResponseEntity<Map<String, Object>> getAllPeoples(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    );

    /************************************************************************************************************/
    @GetMapping("api/searchStock/search")
    ResponseEntity<?> searchStock(@RequestParam("search") String search);

    @PostMapping("/api/metasearch-cache")
    ResponseEntity<Map<String, Object>> postMetasearchData(@RequestBody MetaSearchDTO metaSearchDTO);
}
