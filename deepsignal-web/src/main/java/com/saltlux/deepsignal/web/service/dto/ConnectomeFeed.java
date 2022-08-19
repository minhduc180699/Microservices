package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeFeed {

    private String id;

    private String connectomeId;

    private String docType;

    private String docId;

    private List<TopicSimModel> topicSims;

    private Double w2vSim;
    private Double gmuseSim;
    private Double score;
    private Double esaSim;
    private String topic;

    private String sourceId;

    private String commentId;

    private String replyId;

    private Map<String, Integer> publishedAt;

    private List<String> projectIds;

    private String webSourceId;

    private List<String> webSourceCategory;

    private Map<String, Integer> collectedAt;

    private String serviceType;

    private String sourceUri;

    private String title;
    private String content;
    private Long timestamp;
    private String serviceLanguage;
    //    private String writer;

    private List<String> imageLinks;
    //    private List<ImageDTO> imageDTOS;

    //    @JsonProperty(value = "post_type")
    //    private Integer postType;

    private List<StockCodes> stockCodes;
}

@Getter
@Setter
class TopicSimModel {

    private String topic;
    private String w2vSim;
    private String asaSim;
}
