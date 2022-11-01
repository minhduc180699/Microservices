package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.api.client.DsFeedClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/connectome-feeds")
@Tag(name = "Connectome Feed Management", description = "The Connectome Feed management API")
public class FeedResource {

    private final DsFeedClient dsFeedClient;

    public FeedResource(@Qualifier DsFeedClient dsFeedClient) {
        this.dsFeedClient = dsFeedClient;
    }

    private final Logger log = LoggerFactory.getLogger(FeedResource.class);

    @GetMapping("/getListFeeds/{connectomeId}")
    @Operation(
        summary = "Get all feed information ",
        tags = { "Connectome Feed Management" },
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> getAllFeedByConnectomeId(
        @PathVariable(value = "connectomeId", required = true) String connectomeId,
        @RequestParam(value = "requestId", required = false) String request_id,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "from", required = false) String from,
        @RequestParam(value = "until", required = false) String until,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "search_type", required = false) String searchType,
        @RequestParam(value = "channels", required = false) String channels,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "lang", required = true, defaultValue = "en") String lang
    ) {
        try {
            if (Objects.isNull(connectomeId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(
                dsFeedClient.searchFeed(connectomeId, request_id, keyword, from, until, page, size, searchType, channels, type, lang)
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getDetailFeed/{connectomeId}")
    @Operation(summary = "Search card by docId", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getDetailCard(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "requestId",required = false) String requestId,
        @RequestParam(value = "docId") String docId){

        if (StringUtils.isEmpty(connectomeId) || StringUtils.isEmpty(docId)){
                return ResponseEntity.badRequest().body("ConncetomeId or docId is null");
        }
        try {
            return ResponseEntity.ok(dsFeedClient.getFeed(connectomeId, requestId, docId));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/getListDocumentByIds")
    @Operation(summary = "Get all feed information in list", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> getAllFeedByIds(@RequestBody JSONObject bodyJSON) {
        try {
            return ResponseEntity.ok(dsFeedClient.getListDocumentByIds(bodyJSON));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/activityLike")
    @Operation(summary = "handle interaction", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
        public ResponseEntity<?> handleActivityLike(@RequestBody JSONObject bodyJSON){
        try {
            return ResponseEntity.ok(dsFeedClient.handleActivityLike(bodyJSON));
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/activityBookmark")
    @Operation(summary = "handle interaction", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> handleActivityBookmark(@RequestBody JSONObject bodyJSON){
        try {
            return ResponseEntity.ok(dsFeedClient.handleActivityBookmark(bodyJSON));
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/hideFeed")
    @Operation(summary = "handle interaction", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> handleHideDoc(@RequestBody JSONObject bodyJSON){
        try {
            return ResponseEntity.ok(dsFeedClient.handleHideDoc(bodyJSON));
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
