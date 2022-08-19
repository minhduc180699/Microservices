package com.saltlux.deepsignal.adapter.domain.EntityLinking;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class CandidateDTO {

    @Field("entity_title")
    private String entityTitle;

    @Field("entity_id")
    private String entityId;

    private double score;

    @Field("meta")
    private MetaDTO meta;
}
