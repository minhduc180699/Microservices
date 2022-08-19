package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityLinking {

    private Keyword keyword;
    private List<Candidate> candidates;
}
