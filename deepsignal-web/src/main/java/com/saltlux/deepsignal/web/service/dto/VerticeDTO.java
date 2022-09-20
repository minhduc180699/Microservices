package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerticeDTO {

    private String label;

    private double dfCnt;

    private double weight;

    private String type;

    private String mainCluster;

    private List<String> entities;

    private List<String> clusters;

    private Boolean favorite;

    private Boolean disable;
}
