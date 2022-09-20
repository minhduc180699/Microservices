package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeOnTextRequestBody {

    private String connectomeId;
    private List<String> documentIds;
    private String title;
    private String content;
}
