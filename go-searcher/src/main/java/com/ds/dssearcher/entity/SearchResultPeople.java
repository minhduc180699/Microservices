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
public class SearchResultPeople extends ResponseResult{
    private List<DocumentPeopleEntity> data = null;

    public SearchResultPeople(String result, Integer result_code, List<DocumentPeopleEntity> data) {
        super(result, result_code);
        this.data = data;
    }
}
