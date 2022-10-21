package com.saltlux.deepsignal.web.service.dto;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A File upload
 */
@Getter
@Setter
public class PersonalDocument {

    private String id;

    private String title;

    private String url;

    private String idHash;

    private String docId;

    private String filePath;

    private String createdDate;

    private String author;

    private String content;

    private String contentSummary;

    private String keyword;

    private String connectomeId;

    private String language;

    private String originDate;

    private String searchType;

    private String type;

    private String faviconUrl;

    private String faviconBase64;

    private String ogImageBase64;

    private String ogImageUrl;

    private List<String> imageUrl;

    private String imageBase64;

    private Long uniqueKey;

    private Instant publishedAt;

    //private String responseEntityLinking;

    private Integer isDelete;

    private int liked = 0;
    private boolean bookmarked;
    private boolean memo;
    private boolean deleted;
}
