package com.saltlux.deepsignal.feedcache.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DocModel {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String _id;
    private String connectomeId;
//    private String requestId = "NULL";
    private String docId;
    private String type;
    private String url;
    private String feed_content_id;
    private Integer liked;
    private Boolean isBookmarked;
    private Boolean isRecommend;
    private Boolean isDeleted;
    private Boolean isFeed;
    private String created_date;
    private String created_by;
//    private String service_language;
    private String lang;
    private String keyword;
    private String search_type;
    private String published_at;
    private String channel;
    private String collector;
    private String title;
    private String writer;
//    private String writer_search;
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

    public DocModel mergeDoc(DocModel docModel){
        if(docModel == null){
            return this;
        }
        if(docModel.type != null){
            this.type = docModel.type;
        }
        if(docModel.url != null){
            this.url = docModel.url;
        }
        if(docModel.feed_content_id != null){
            this.feed_content_id = docModel.feed_content_id;
        }
        if(docModel.liked != null){
            this.liked = docModel.liked;
        }
        if(docModel.isBookmarked != null){
            this.isBookmarked = docModel.isBookmarked;
        }
        if(docModel.isRecommend != null){
            this.isRecommend = docModel.isRecommend;
        }
        if(docModel.isDeleted != null){
            this.isDeleted = docModel.isDeleted;
        }
        if(docModel.isFeed != null){
            this.isFeed = docModel.isFeed;
        }
        if(docModel.created_date != null){
            this.created_date = docModel.created_date;
        }
        if(docModel.created_by != null){
            this.created_by = docModel.created_by;
        }
        if(docModel.lang != null){
            this.lang = docModel.lang;
        }
        if(docModel.keyword != null){
            this.keyword = docModel.keyword;
        }
        if(docModel.search_type != null){
            this.search_type = docModel.search_type;
        }
        if(docModel.published_at != null){
            this.published_at = docModel.published_at;
        }
        if(docModel.channel != null){
            this.channel = docModel.channel;
        }
        if(docModel.collector != null){
            this.collector = docModel.collector;
        }
        if(docModel.title != null){
            this.title = docModel.title;
        }
        if(docModel.writer != null){
            this.writer = docModel.writer;
        }
        if(docModel.og_image_url != null){
            this.og_image_url = docModel.og_image_url;
        }
        if(docModel.og_image_base64 != null){
            this.og_image_base64 = docModel.og_image_base64;
        }
        if(docModel.favicon_url != null){
            this.favicon_url = docModel.favicon_url;
        }
        if(docModel.favicon_base64 != null){
            this.favicon_base64 = docModel.favicon_base64;
        }
        if(docModel.description != null){
            this.description = docModel.description;
        }
        if(docModel.feed_partition != null){
            this.feed_partition = docModel.feed_partition;
        }
        if(docModel.content_partition != null){
            this.content_partition = docModel.content_partition;
        }
        if(docModel.tags != null){
            this.tags = docModel.tags;
        }
        if(docModel.feed_id != null){
            this.feed_id = docModel.feed_id;
        }
        if(docModel.writer_id != null){
            this.writer_id = docModel.writer_id;
        }
        return this;
    }
}