package com.saltlux.deepsignal.adapter.service.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class WebSourceConditionDTO {

    @Size(min = 1, max = 512)
    private String condition;

    @Size(min = 1, max = 256)
    private String category;

    @Size(min = 1, max = 50)
    private String webSourceName;

    private String connectomeId;

    private String connectomeName;
}
