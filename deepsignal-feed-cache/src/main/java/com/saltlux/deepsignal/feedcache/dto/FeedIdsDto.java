package com.saltlux.deepsignal.feedcache.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedIdsDto {
    private String request_id;
    private List<String> docIds;
    private String connectomeId;
    private Integer size = 10;
    private Integer page = 0;
}
