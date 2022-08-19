package com.saltlux.deepsignal.adapter.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("connectome")
public class Connectome {

    @Id
    @Field("_id")
    private String id;

    @Field("connectome_id")
    private String connectomeId;

    @Field("lang")
    private String lang;

    @Field("connectome_status")
    private ConnectomeStatus connectomeStatus;

    @DBRef
    private List<Vertex> vertices;
    // @DBRef
    // private List<Edge> edges;
}

@Getter
@Setter
@Document("connectome_status")
class ConnectomeStatus {

    @Field("connectome_id")
    private String connectomeId;

    @Field("status")
    private String status;

    @Field("lang")
    private String lang;

    @Temporal(TemporalType.TIMESTAMP)
    @Field("first_created_date")
    private Date firstCreatedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Field("when_learning_started")
    private Date whenLearningStarted;

    @Temporal(TemporalType.TIMESTAMP)
    @Field("last_updated_at")
    private Date lastUpdatedAt;

    @Field("number_of_times_connectome_was_updated")
    private Number number;

    @Field("number_of_documents")
    private Number numberOfDocuments;

    @Field("number_of_feeds")
    private Number numberOfFeeds;

    @Field("number_of_unique_entities")
    private Number numberOfUniqueEntities;
}
