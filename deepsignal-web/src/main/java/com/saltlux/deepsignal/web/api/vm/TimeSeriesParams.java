package com.saltlux.deepsignal.web.api.vm;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSeriesParams implements Serializable {

    private String connectome_id;
    private String compkeywords;
    private String search_source;
    private String country;
    private String from; //2020-01-15
    private String until;
    private String request_id;
}
