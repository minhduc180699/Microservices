package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feed {

    private String id;

    private String connectomeId;

    private String docId;

    private Long timestamp;

    private String topic;

    private Double score;

    private List<TopicSims> topicSims;

    private String title;

    private String content;

    private String writerName;

    private String serviceType;

    private List<StockCodes> stockCodes;

    private PublishedAt publishedAt;

    private List<String> imageLinks;

    private String sourceId;

    private String favicon;

    private int liked;

    private SharingMethod shared;

    private boolean bookmarked;

    private boolean deleted;

    private Integer memo;

    private String lang;

    private String orgDate;

    private String recommendDate;

    private String searchKeyword;

    private String searchType;

    //private List<EntityLinking> entityLinking;

    private String recommendationType;

    private List<ClusterDocument> clusterDocuments;

    private List<WordCloud> wordClouds;

    private NeuronNetworkChart neuronNetworkChart;

    private String htmlContent;

    private String dsWebId;

    private Related relatedPeople;

    private Related relatedCompany;

    private String collectionType;
}

@Getter
@Setter
class TopicSims {

    private String topic;
    private Double score;
}

@Getter
@Setter
class SharingMethod {

    private boolean facebook;
    private boolean twitter;
    private boolean linkedIn;
    private boolean link;
}

@Getter
@Setter
class Related {

    private List<String> famous;
    private List<String> anonymous;
}
