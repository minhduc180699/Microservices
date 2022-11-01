package com.ds.dssearcher.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentRealtimeSearchEntity {
    private Long requestId;
    private Long agentId;
    private String id_str;
    private String text;
    private Integer is_read ;
    private String json_full;
    private String lang;
    private String retweet_count;
    private String favorite_count;
    private String screen_name;
    private String screen_name_lower;
    private String author_id;
    private String reply_count;
    private String quote_count;
    private Long __unique__key;
    private String created_date;
    private String tweet_date;
    private String created_at_parsed;
    private List<String> image_urls = new LinkedList<>();
}
