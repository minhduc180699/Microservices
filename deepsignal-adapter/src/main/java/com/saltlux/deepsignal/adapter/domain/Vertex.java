package com.saltlux.deepsignal.adapter.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("vertices")
public class Vertex {

    @Field("label")
    private String label;

    @Field("dfCnt")
    private Number dfCnt;

    @Field("type")
    private String type;

    @Field("main_cluster")
    private String mainCluster;

    @Field("entities")
    private List<String> entities;

    @Field("documentIds")
    private List<String> documentIds;

    @Field("feedIds")
    private List<String> feedIds;

    @Field("isFavorite")
    private Boolean isFavorite;

    @Field("isDisable")
    private Boolean isDisable;
}
