package com.saltlux.deepsignal.feedcache.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedModel {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String _id;
    private String connectomeId;
    private String requestId = "NULL";
    private String docId;
    private String type;
    private String feed_content_id;
    private Integer liked;
    private Boolean isBookmarked;
    private Boolean isRecommend;
    private Boolean isDeleted;
    private Boolean isFeed;
    private String created_date;
    private String created_by;
    private String service_language;
    private String keyword;
    private String search_type;
    private String published_at;
    private String channel;
    private String collector;
    private String title;
    private String writer;
    private String writer_search;
    private String og_image_url;
    private String og_image_base64;
    private String favicon_url;
    private String favicon_base64;
    private String description;
    private String feed_partition;
    private String content_partition;
    private String tags;
    private String feed_id;
    private String writer_id;
}