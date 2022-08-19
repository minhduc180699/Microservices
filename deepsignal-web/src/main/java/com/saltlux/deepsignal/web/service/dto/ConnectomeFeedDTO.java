package com.saltlux.deepsignal.web.service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeFeedDTO {

    //    private String id;

    @JsonProperty(value = "source_id")
    private String sourceId;

    @JsonProperty(value = "comment_id")
    private String commentId;

    @JsonProperty(value = "reply_id")
    private String replyId;

    @JsonProperty(value = "published_at")
    private Map<String, Integer> publishedAt;

    @JsonProperty(value = "crawler_id")
    private String crawlerId;

    @JsonProperty(value = "project_ids")
    private List<String> projectIds;

    @JsonProperty(value = "web_source_id")
    private String webSourceId;

    @JsonProperty(value = "web_source_category")
    private List<String> webSourceCategory;

    @JsonProperty(value = "collected_at")
    private Map<String, Integer> collectedAt;

    @JsonProperty(value = "service_type")
    private String serviceType;

    @JsonProperty(value = "source_uri")
    private String sourceUri;

    private String title;
    private String content;

    @JsonProperty(value = "writer_name")
    @JsonAlias("writer")
    private String writer;

    @JsonProperty(value = "image_links")
    private List<String> imageLinks;

    private List<ImageDTO> imageDTOS;

    @JsonProperty(value = "post_type")
    private Integer postType;

    @JsonProperty(value = "stock_code")
    private Integer stockCode;

    @JsonProperty(value = "service_language")
    private String serviceLanguage;
}
