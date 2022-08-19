package com.saltlux.deepsignal.adapter.domain;

import java.util.List;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("connectome_feed")
public class ConnectomeFeed {

    @Id
    @Field("_id")
    private String id;

    @Field("connectome_id")
    private String connectomeId;

    @Field("doc_type")
    private String docType;

    @Field("doc_id")
    private String docId;

    @Field("topic_sims")
    private List<TopicSimModel> topicSims;

    @Field("w2v_sim")
    private Double w2vSim;

    @Field("esa_sim")
    private Double esaSim;

    private String topic;

    @Field("source_uri")
    private String sourceUri;

    private String title;
    private String content;
    private Long timestamp;

    @Field("source_id")
    private String sourceId;

    @Field("comment_id")
    private String commentId;

    @Field("reply_id")
    private String replyId;

    @Field("published_at")
    private PublishedAt publishedAt;

    @Field("project_ids")
    private List<String> projectIds;

    @Field("web_source_id")
    private String webSourceId;

    @Field("web_source_category")
    private List<String> webSourceCategory;

    @Field("collected_at")
    private PublishedAt collectedAt;

    @Field("stock_codes")
    private List<StockCodes> stockCodes;

    @Field("service_type")
    private String serviceType;

    @Field("service_language")
    private String serviceLanguage;

    @Field("image_links")
    private List<String> imageLinks;

    @Field("gmuse_sim")
    private Double gmuseSim;

    @Field("score")
    private Double score;
}

@Getter
@Setter
@Document("topic_sims")
class TopicSimModel {

    private String topic;
    private String w2vSim;
    private String asaSim;
}
