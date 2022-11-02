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
public class SearchResultDataSource extends ResponseResult{
    private List<DocumentSourceDataEntity> data = null;
    private Integer total;
    private Long totalAll;

    public SearchResultDataSource(String result, Integer result_code, List<DocumentSourceDataEntity> data, Integer total, Long totalAll) {
        super(result, result_code);
        this.data = data;
        this.total = total;
        this.totalAll = totalAll;
    }
}
