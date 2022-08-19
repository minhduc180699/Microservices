package com.saltlux.deepsignal.adapter.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityResponse {

    private List<ActivityDTO> activityList;
    private long totalItems;
}
