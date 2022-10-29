package com.saltlux.deepsignal.feedcache.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestBodyGetListDoc {
    private String requestId;
    @NotEmpty
    private List<String> docIds;
    private String connectomeId = "";
    private Boolean require_content = false;
    private Integer size = 10;
    private Integer page = 0;
}
