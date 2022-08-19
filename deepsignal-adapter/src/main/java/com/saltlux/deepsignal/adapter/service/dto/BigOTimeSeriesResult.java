package com.saltlux.deepsignal.adapter.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BigOTimeSeriesResult implements Serializable {

    private String keyword;
    private LinkedHashMap<String, Long> data;

    public BigOTimeSeriesResult(String keyword, LinkedHashMap<String, Long> data) {
        this.keyword = keyword;
        this.data = data;
    }
}
