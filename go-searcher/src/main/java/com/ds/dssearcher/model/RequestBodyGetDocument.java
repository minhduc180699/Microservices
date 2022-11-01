package com.ds.dssearcher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestBodyGetDocument {
    private String requestId;
    @NotEmpty
    private String docId;
    private String connectomeId= "";
    private Boolean require_content = false;
}
