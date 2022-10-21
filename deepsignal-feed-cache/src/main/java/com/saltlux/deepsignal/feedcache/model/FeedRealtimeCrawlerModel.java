package com.saltlux.deepsignal.feedcache.model;

import lombok.Data;

@Data
public class FeedRealtimeCrawlerModel {
    private Long id;
    private String name;
    private Double size;
    private String path;
    private String mineType;
    private String fileType;
    private String downloadUrl;
    private String description;
    private String type;
    private String createdDate;
    private String lastModifiedDate;
    private String connectomeId;
    private String author;
    private String date;
    private String originDate;
    private String publishedDate;
    private String searchType;
    private String favicon;
    private String img;
    private String lang;
    private String keyword;
    private String requestBy;
}
