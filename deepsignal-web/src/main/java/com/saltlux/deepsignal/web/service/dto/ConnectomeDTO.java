package com.saltlux.deepsignal.web.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeDTO {

    private String connectomeId;

    private String connectomeName;

    private String connectomeJob;

    private String description;

    private UserDTO user;
}
