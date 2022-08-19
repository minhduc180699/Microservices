package com.saltlux.deepsignal.web.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeatmapDTO {

    private Long id;

    private int x;

    private int y;

    private int value;
    //    private String userId;
}
