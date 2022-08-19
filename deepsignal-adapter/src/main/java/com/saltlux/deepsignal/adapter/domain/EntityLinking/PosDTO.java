package com.saltlux.deepsignal.adapter.domain.EntityLinking;

import java.util.List;
import lombok.Data;

@Data
public class PosDTO {

    private List<String> universal;
    private List<String> treebank;
}
