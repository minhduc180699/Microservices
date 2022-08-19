package com.saltlux.deepsignal.web.api.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockPriceVM {

    private String marketName;
    private String symbolCode;
    private String interval;
    private String maxRecord;
}
