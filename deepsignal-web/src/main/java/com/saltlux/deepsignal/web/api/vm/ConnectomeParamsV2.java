package com.saltlux.deepsignal.web.api.vm;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectomeParamsV2 {

    private String connectome_id;
    private String sourceLang;
    private List<String> targetLangList;
}
