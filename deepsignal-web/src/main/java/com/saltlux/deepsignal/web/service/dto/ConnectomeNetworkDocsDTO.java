package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeNetworkDocsDTO {

    private String connectomeId;

    private ConnectomeStatusDTO connectomeStatus;

    private List<VerticeDocsDTO> vertices;
}
