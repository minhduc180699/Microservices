package com.saltlux.deepsignal.web.api.vm;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntitiesDetailsParams {

    private String sourceLang;
    private List<String> titles;
    private List<String> targetLangList;
}
