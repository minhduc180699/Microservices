package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeNodeDTO {

    private String label;

    private List<String> weight;

    private List<String> linkedNodes;

    private Boolean favorite;

    private Boolean disable;
}
