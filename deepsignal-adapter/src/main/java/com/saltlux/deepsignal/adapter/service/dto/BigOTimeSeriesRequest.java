package com.saltlux.deepsignal.adapter.service.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BigOTimeSeriesRequest implements Serializable {

    private String query;
    private String language;
    private String interval = "month";
    private String timezone = "Asia/Seoul";
    private Date from;
    private Date until;
}
