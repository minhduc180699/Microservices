package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class miniConnectomeRequestBody {

    private String sourceLang;
    private List<String> ids;
}
