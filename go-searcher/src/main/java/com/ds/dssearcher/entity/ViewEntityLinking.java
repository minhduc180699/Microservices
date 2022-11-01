package com.ds.dssearcher.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ViewEntityLinking {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ResponseResult{
        private String request_id;
        private String doc_id;

        public Response(String result, Integer result_code, String request_id, String doc_id) {
            super(result, result_code);
            this.request_id = request_id;
            this.doc_id = doc_id;
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseEntityLinking extends Response {
        private EntityLinking.ResponseEntityLinking entityLinking;

        public ResponseEntityLinking(String result, Integer result_code, String request_id, String doc_id, EntityLinking.ResponseEntityLinking entityLinking) {
            super(result, result_code, request_id, doc_id);
            this.entityLinking = entityLinking;
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseEntityLinkingSummary extends Response {
        private EntityLinking.ResponseEntityLinkingSummary entityLinking_summary;

        public ResponseEntityLinkingSummary(String result, Integer result_code, String request_id, String doc_id, EntityLinking.ResponseEntityLinkingSummary entityLinking_summary) {
            super(result, result_code, request_id, doc_id);
            this.entityLinking_summary = entityLinking_summary;
        }
    }

}
