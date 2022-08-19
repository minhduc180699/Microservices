package com.saltlux.deepsignal.adapter.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BigOScrollRequest implements Serializable {

    private String scrollId;
    private String query;
    private String language;
}
