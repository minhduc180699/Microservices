package com.ds.dssearcher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

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
    private Integer size;
    private Integer page;
}
