package com.saltlux.deepsignal.adapter.domain.connectome;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "connectome_feed")
public class ConnectomeFeed_1 {

    @Id
    @Field("_id")
    private String id;

    @Field("card_id")
    private String cardId;

    @Field("card_shape")
    private String cardShape;

    @Temporal(TemporalType.TIMESTAMP)
    @Field("timestamp")
    private Date timeStamp;

    @Field("connectome_id")
    private String connectomeId;

    private HashMap<String, String> titlebar;

    @DBRef
    private List<Post> post;
}
