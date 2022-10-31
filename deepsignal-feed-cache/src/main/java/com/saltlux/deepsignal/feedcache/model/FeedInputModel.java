package com.saltlux.deepsignal.feedcache.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedInputModel {
    private String requestId;
    @NotEmpty
    private String connectomeId;
    @NotEmpty
    private String docId;
    private Integer liked;
    private Boolean isBookmarked;
    private Boolean isDeleted;
}
