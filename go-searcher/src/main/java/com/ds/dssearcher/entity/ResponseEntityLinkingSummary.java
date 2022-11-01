package com.ds.dssearcher.entity;

public class ResponseEntityLinkingSummary extends ResponseResult {
    private String request_id;
    private String doc_id;
    private EntityLinking.ResponseEntityLinkingSummary entityLinking_summary;

    public ResponseEntityLinkingSummary(String result, Integer result_code, String request_id, String doc_id, EntityLinking.ResponseEntityLinkingSummary entityLinking_summary) {
        super(result, result_code);
        this.request_id = request_id;
        this.doc_id = doc_id;
        this.entityLinking_summary = entityLinking_summary;
    }
}
