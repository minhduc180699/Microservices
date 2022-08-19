package com.saltlux.deepsignal.web.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class entityUpdateRequestBody {

    private String vertexLabel;

    private String connectomeId;

    private String sourceLang;
}
