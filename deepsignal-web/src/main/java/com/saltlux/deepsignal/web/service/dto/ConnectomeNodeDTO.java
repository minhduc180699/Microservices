package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeNodeDTO {

    private String id;

    private String keyId;

    private String label;

    private String keyLabel;

    private List<String> relatedDocuments;

    private List<String> linkedNodes;

    private Number weight;
}
