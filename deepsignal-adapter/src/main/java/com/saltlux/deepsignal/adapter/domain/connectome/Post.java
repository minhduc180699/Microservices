package com.saltlux.deepsignal.adapter.domain.connectome;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("post")
public class Post {

    @Field("post_type")
    private String postType;

    @Field("post_id")
    private String postId;

    @Field("post_important")
    private Double postImportant;

    @Field("source_uri")
    private String sourceUri;

    @Temporal(TemporalType.TIMESTAMP)
    @Field("published_at")
    private Date publishedAt;

    private List<HashMap<String, String>> medias;

    private HashMap<String, String> title;

    private HashMap<String, String> content;
}
