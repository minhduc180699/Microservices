package com.saltlux.deepsignal.adapter.domain;

import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.EntityLinking.EntityLinkingDTO;
import com.saltlux.deepsignal.adapter.service.dto.SharingMethod;
import java.time.Instant;
import java.util.List;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("feed")
public class Feed {

    @Id
    @Field("_id")
    private String id;

    @Field("connectome_id")
    private String connectomeId;

    @Field("doc_id")
    private String docId;

    //    private Long timestamp;
    //
    //    private String topic;
    //
    //    private Double score;

    @Field("topic_sims")
    private List<TopicSims> topicSims;

    private String title;

    private String content;

    @Field("writer_name")
    private String writerName;

    //    @Field("service_type")
    //    private String serviceType;

    @Field("stock_code")
    private List<StockCodes> stockCodes;

    //    @Field("published_at")
    //    private PublishedAt publishedAt;

    @Field("image_links")
    private List<String> imageLinks;

    @Field("source_id")
    private String sourceId;

    private String favicon;

    private int liked = 0;

    private SharingMethod shared;

    private boolean bookmarked;

    private boolean deleted;

    private int memo;

    private String lang;

    // @Field("entitylinking")
    // private List<EntityLinkingDTO> entityLinking;

    @Field("search_keyword")
    private String searchKeyword;

    @Field("org_date")
    private String orgDate;

    @Field("search_type")
    private String searchType;

    @Field("recommend_date")
    private Instant recommendDate;

    @Field("recommendation_type")
    private String recommendationType;

    @Field("cluster_documents")
    private List<ClusterDocument> clusterDocuments;

    @Field("word_cloud")
    private List<WordCloud> wordClouds;

    @Field("ner_net_chart")
    private NeuronNetworkChart neuronNetworkChart;

    @Field("html_content")
    private String htmlContent;

    @Field("dsweb_id")
    private String dsWebId;

    @Field("related_people")
    private Related relatedPeople;

    @Field("related_company")
    private Related relatedCompany;

    private String collectionType = Constants.page.FEED.type;
}

@Getter
@Setter
@Document("topic_sims")
class TopicSims {

    private String topic;
    private Double score;
}

@Getter
@Setter
class Related {

    private List<String> famous;
    private List<String> anonymous;
}
