package com.saltlux.deepsignal.feedcache.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.JsonObject;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedDto {
    private String connectomeId;
    private String request_id;
    private JsonObject feed;
    private String feed_id;
    private List<String> ids;
    private Integer size = 10;
    private Integer page = 1;
}
