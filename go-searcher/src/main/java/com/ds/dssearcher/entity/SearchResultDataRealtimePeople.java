package com.ds.dssearcher.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDataRealtimePeople extends ResponseResult {
    private Integer requestId;
    private List<DocumentRealtimePeopleSearchEntity> data;

    public SearchResultDataRealtimePeople(String result, Integer result_code, Integer requestId, List<DocumentRealtimePeopleSearchEntity> data) {
        super(result, result_code);
        this.requestId = requestId;
        this.data = data;
    }
}
