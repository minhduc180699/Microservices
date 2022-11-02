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
public class SearchResult extends ResponseResult{
    private List<DocumentEntity> data = null;

    public SearchResult(String result, Integer result_code, List<DocumentEntity> data) {
        super(result, result_code);
        this.data = data;
    }
}
