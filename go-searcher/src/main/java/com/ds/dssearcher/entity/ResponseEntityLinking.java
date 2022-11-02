package com.ds.dssearcher.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntityLinking extends ResponseResult{
    private String request_id;
    private String doc_id;
    private EntityLinking.ResponseEntityLinking entityLinking;
    private EntityLinking.ResponseEntityLinkingSummary entityLinking_summary;

    public ResponseEntityLinking(String result, Integer result_code, String request_id, String doc_id, EntityLinking.ResponseEntityLinking entityLinking, EntityLinking.ResponseEntityLinkingSummary entityLinking_summary) {
        super(result, result_code);
        this.request_id = request_id;
        this.doc_id = doc_id;
        this.entityLinking = entityLinking;
        this.entityLinking_summary = entityLinking_summary;
    }
}
