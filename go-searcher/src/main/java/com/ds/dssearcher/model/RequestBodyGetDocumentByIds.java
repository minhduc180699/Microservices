package com.ds.dssearcher.model;

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
public class RequestBodyGetDocumentByIds {
    @NotEmpty
    private List<String> docIds;
    @NotEmpty
    private String connectomeId;
    private Integer page;
    private Integer size;
}
