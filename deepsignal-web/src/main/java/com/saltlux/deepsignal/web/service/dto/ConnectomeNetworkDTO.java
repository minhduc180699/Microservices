package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeNetworkDTO {

    private String connectomeId;

    private ConnectomeStatusDTO connectomeStatus;

    private List<VerticeDTO> vertices;

    private List<EdgeDTO> edges;
}
