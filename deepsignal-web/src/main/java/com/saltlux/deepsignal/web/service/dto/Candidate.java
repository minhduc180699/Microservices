package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class Candidate {

    private String entityTitle;

    private String entityId;

    private double score;

    private Meta meta;
}

@Data
class Meta {

    private List<StockCodes> stock;
}
