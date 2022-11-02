package com.ds.dssearcher.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentRealtimeCrawlerEntity{
    private Long agentId;
    private String text;
    private String created_at;
    private String id_str;
    private String lang;
    private String screen_name;
    private Integer retweet_count;
    private Integer favorite_count;
    private String author_id;
    private String json_full;
    private String screen_name_lower;
    private String reply_count;
    private String quote_count;
    private Long __unique__key;
    private String created_at_parsed;
}
