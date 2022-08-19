package com.saltlux.deepsignal.adapter.domain.dstornado;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "deepsignal_realtime_metasearch_#{T(com.saltlux.deepsignal.adapter.util.Utility).getLang()}")
public class DsRealtimeMetaSearch {

    @Id
    @Field("_id")
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("img")
    private String img;

    @Field("favicon")
    private String favicon;

    @Field("link")
    private String link;

    @Field("author")
    private String author;

    @Field("org_date")
    private String orgDate;

    @Field("metasearch")
    private boolean metasearch;

    @Field("dateTime")
    private String dateTime;

    @Field("searchType")
    private String searchType;

    @Field("__unique__key")
    private String __unique__key;

    @Field("keyword")
    private String keyword;

    @Field("lang")
    private String lang;
}
