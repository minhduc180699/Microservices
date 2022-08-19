package com.saltlux.deepsignal.adapter.domain.EntityLinking;

import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class KeyWordDTO {

    private String word;
    private String lemma;
    private SpanDTO span;

    @Field("word_id")
    private String wordId;

    private int status;
    private String ner;
    private PosDTO pos;

    @Field("candidate_ids")
    private List<String> candidateIds;
}
