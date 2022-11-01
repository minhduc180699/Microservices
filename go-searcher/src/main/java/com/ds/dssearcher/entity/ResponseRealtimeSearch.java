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
public class ResponseRealtimeSearch extends ResponseResult{

    private List<DocumentRealtimeSearchEntity> data;

}
