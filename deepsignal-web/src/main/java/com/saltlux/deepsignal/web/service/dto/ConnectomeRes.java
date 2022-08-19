package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConnectomeRes extends CommonRes {

    private List<Connectome> connectomes;
}
