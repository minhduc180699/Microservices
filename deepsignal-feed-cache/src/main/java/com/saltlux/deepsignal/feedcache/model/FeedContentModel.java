package com.saltlux.deepsignal.feedcache.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedContentModel {
    private String _id;
    private String web_source_category;
    private String favicon_url;
    private String category_url;
    private String channel;
    private String description;
    private String title;
    private String source_uri;
    private String subdomain_url;
    private String content;
    private String collector;
    private String feed_id;
    private String html_content;
    private String video_url;
    private List<String> image_base64;
    private String domain_url;
    private String project_id;
    private String web_source_id;
    private String og_image_base64;
    private String published_at;
    private String keyword;
    private String writer_id;
    private String og_image_url;
    private String service_language;
    private String favicon_base64;
    private List<String> image_url;
    private String video_base64;
    private String comment_id;
    private String subcategory_url;
    private String search_type;
    private String collected_at;
    private String tags;
    private String content_search;
    private String service_type;
    private String origin_date;
    private String reply_id;
    private String source_id;
    private String writer;
    private String category;
    private String writer_search;
    private String lang;
}