package com.ds.dssearcher.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDataRealtimeKeyword extends ResponseResult{
    private List<DocumentRealtimeKeywordSearchEntity> data;

    public SearchResultDataRealtimeKeyword(String result, Integer result_code, List<DocumentRealtimeKeywordSearchEntity> data) {
        super(result, result_code);
        this.data = data;
    }
}
