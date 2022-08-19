package com.saltlux.deepsignal.adapter.domain;

import java.util.List;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("signal_todayissue")
public class SignalTodayIssue {

    @Id
    @Field("_id")
    private String id;

    @Field("connectome_id")
    private String connectomeId;

    @Field("cluster_name")
    private String keywords;

    @Field("signal_type")
    private String signalType;

    @Field("signal_id")
    private Integer signalId;

    @Field("work_day")
    private String workDay;

    @Field("display_order")
    private int displayOrder;

    @Field("cluster_documents")
    private List<ClusterDocument> clusterDocuments;

    @Field("word_cloud")
    private List<WordCloud> wordClouds;

    @Field("ner_net_chart")
    private NeuronNetworkChart neuronNetworkChart;

    @Field("cluster_title")
    private String mainKeyword;

    @Field("isDelete")
    private Integer isDelete;

    // This field using to map with mysql database
    //    private transient String keywords;
    //    private transient String mainKeyword;

    @PostLoad
    public void onLoad() {
        //        this.keywords = this.getClusterName();
        //        this.mainKeyword = this.getClusterTitle();
    }
}

@Getter
@Setter
@Document("cluster_documents")
class ClusterDocument {

    @Id
    @Field("_id")
    private String id;

    @Field("doc_obj_id")
    private String objId;

    @Field("connectome_id")
    private String connectomeId;

    @Field("doc_id")
    private String docId;

    @Field("title")
    private String title;

    @Field("writer_name")
    private String writerName;

    @Field("content")
    private String content;

    @Field("source_id")
    private String sourceId;

    @Field("image_links")
    private List<String> imageLinks;

    @Field("favicon")
    private String favicon;

    @Field("lang")
    private String lang;

    @Field("words")
    private String words;
}

@Getter
@Setter
@Document("word_cloud")
class WordCloud {

    @Field("word_name")
    private String wordName;

    @Field("word_count")
    private int wordCount;
}

@Getter
@Setter
@Document("ner_net_chart")
class NeuronNetworkChart {

    @Field("nodes")
    private List<Node> nodes;

    @Field("links")
    private List<Link> links;
}

@Getter
@Setter
@Document("nodes")
class Node {

    @Id
    @Field("_id")
    private String id;

    private String name;
    private String url;
    private boolean loaded;

    @Field("nerType")
    private String nerType;
}

@Getter
@Setter
@Document("links")
class Link {

    @Id
    @Field("_id")
    private String id;

    private String from;
    private String to;
}
