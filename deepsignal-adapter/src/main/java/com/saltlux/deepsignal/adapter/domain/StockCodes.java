package com.saltlux.deepsignal.adapter.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class StockCodes {

    private String market;

    @Field("stock_code")
    private String stockCode;

    @Field("isin")
    private String isin;

    private int freq;
}
