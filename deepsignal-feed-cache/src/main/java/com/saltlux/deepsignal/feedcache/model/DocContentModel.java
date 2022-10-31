package com.saltlux.deepsignal.feedcache.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocContentModel {
    private String docId;
    private String title;
    private String source_uri;
    private String content;
    private String collector;
    private String feed_id;
    private String html_content;
    private String video_url;
    private List<String> image_base64;
    private String og_image_base64;
    private String published_at;
    private String keyword;
    private String writer_id;
    private String og_image_url;
    private String lang;
    private String favicon_base64;
    private List<String> image_url;
    private String video_base64;
    private String search_type;
    private String collected_at;
    private String tags;
    private String service_type;
    private String origin_date;
    private String source_id;
    private String writer;
    private String category;
    private String content_partition;
    private String description;
//    private String reply_id;
//    private String content_search;
//    private String comment_id;
//    private String subcategory_url;
//    private String service_language;
//    private String domain_url;
//    private String project_id;
//    private String web_source_id;
//    private String subdomain_url;
//    private String web_source_category;
//    private String favicon_url;
//    private String category_url;
//    private String channel;

    public DocContentModel mergeDocContent(DocContentModel docContentModel) {
        if(docContentModel == null){
             return this;
        }
        if (docContentModel.title != null) {
            this.title = docContentModel.title;
        }
        if (docContentModel.source_uri != null) {
            this.source_uri = docContentModel.source_uri;
        }
        if (docContentModel.content != null) {
            this.content = docContentModel.content;
        }
        if (docContentModel.collector != null) {
            this.collector = docContentModel.collector;
        }
        if (docContentModel.feed_id != null) {
            this.feed_id = docContentModel.feed_id;
        }
        if (docContentModel.html_content != null) {
            this.html_content = docContentModel.html_content;
        }
        if (docContentModel.video_url != null) {
            this.video_url = docContentModel.video_url;
        }
        if (docContentModel.image_base64 != null) {
            this.image_base64 = docContentModel.image_base64;
        }
        if (docContentModel.og_image_base64 != null) {
            this.og_image_base64 = docContentModel.og_image_base64;
        }
        if (docContentModel.published_at != null) {
            this.published_at = docContentModel.published_at;
        }
        if (docContentModel.keyword != null) {
            this.keyword = docContentModel.keyword;
        }
        if (docContentModel.writer_id != null) {
            this.writer_id = docContentModel.writer_id;
        }
        if (docContentModel.og_image_url != null) {
            this.og_image_url = docContentModel.og_image_url;
        }
        if (docContentModel.lang != null) {
            this.lang = docContentModel.lang;
        }
        if (docContentModel.favicon_base64 != null) {
            this.favicon_base64 = docContentModel.favicon_base64;
        }
        if (docContentModel.image_url != null) {
            this.image_url = docContentModel.image_url;
        }
        if (docContentModel.video_base64 != null) {
            this.video_base64 = docContentModel.video_base64;
        }
        if (docContentModel.search_type != null) {
            this.search_type = docContentModel.search_type;
        }
        if (docContentModel.collected_at != null) {
            this.collected_at = docContentModel.collected_at;
        }
        if (docContentModel.tags != null) {
            this.tags = docContentModel.tags;
        }
        if (docContentModel.service_type != null) {
            this.service_type = docContentModel.service_type;
        }
        if (docContentModel.origin_date != null) {
            this.origin_date = docContentModel.origin_date;
        }
        if (docContentModel.source_id != null) {
            this.source_id = docContentModel.source_id;
        }
        if (docContentModel.writer != null) {
            this.writer = docContentModel.writer;
        }
        if (docContentModel.category != null) {
            this.category = docContentModel.category;
        }
        if (docContentModel.content_partition != null) {
            this.content_partition = docContentModel.content_partition;
        }
        if (docContentModel.description != null) {
            this.description = docContentModel.description;
        }
        return this;
    }
}