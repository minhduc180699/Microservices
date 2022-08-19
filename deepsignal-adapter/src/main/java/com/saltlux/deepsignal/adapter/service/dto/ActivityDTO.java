package com.saltlux.deepsignal.adapter.service.dto;

import com.saltlux.deepsignal.adapter.domain.Feed;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActivityDTO extends Feed {

    private String url;

    private String idHash;

    private String filePath;

    private String createdDate;

    private String author;

    private String language;

    private String originDate;

    private String type;

    private String faviconUrl;

    private String faviconBase64;

    private String ogImageBase64;

    private String ogImageUrl;

    private List<String> imageUrl;

    private String imageBase64;

    private Long uniqueKey;

    private Instant publishedAt;

    private String responseEntityLinking;

    private String collectionType;
}
