package com.saltlux.deepsignal.adapter.domain.EntityLinking;

import java.util.List;
import lombok.Data;

@Data
public class EntityLinkingDTO {

    private KeyWordDTO keyword;
    private List<CandidateDTO> candidates;
}
