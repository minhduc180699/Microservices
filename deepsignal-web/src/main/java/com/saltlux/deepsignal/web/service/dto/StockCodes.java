package com.saltlux.deepsignal.web.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockCodes {

    private String market;
    private String stockCode;
    private String isin;
    private int freq;
}
