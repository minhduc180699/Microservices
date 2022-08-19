package com.saltlux.deepsignal.adapter.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaSearchDTO {

    private String keyword;
    private String connectomeId;
    private String lang;
    private String searchType;
    private int page;
}
