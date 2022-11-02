package com.ds.dssearcher.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewDocument {
    private String _id;
    private String title;
    private String description;
    private String content;
    private String writer;
    private String published_at;
    private String source_uri;
    private String lang;
    //    private String img;
//    private String favicon;
    private String search_type;
    private Boolean internalsearch;
    private String keyword;
    private String service_type;
    private Float score;

    private String og_image_url;
    private String og_image_base64;
    private String favicon_base64;
    private String favicon_url;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentLinkingEntity extends ViewDocument {
        private EntityLinking.ResponseEntityLinking entityLinking;
        @JsonIgnore
        private EntityLinking.ResponseEntityLinkingSummary entityLinking_summary;

        public DocumentLinkingEntity(String _id, String title, String description, String content, String writer, String published_at, String source_uri, String lang, String search_type, Boolean internalsearch, String keyword, String service_type, Float score, String og_image_url, String og_image_base64, String favicon_base64, String favicon_url, EntityLinking.ResponseEntityLinking entityLinking) {
            super(_id, title, description, content, writer, published_at, source_uri, lang, search_type, internalsearch, keyword, service_type, score, og_image_url, og_image_base64, favicon_base64, favicon_url);
            this.entityLinking = entityLinking;
        }

        public List<DocumentLinkingEntity> covert(List<DocumentEntity> entities) throws JsonProcessingException {
            if (entities == null) return null;
            List<DocumentLinkingEntity> linkingEntities = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            for (DocumentEntity entity : entities) {
                DocumentLinkingEntity documentLinkingEntity = objectMapper.readValue(objectMapper.writeValueAsString(entity), DocumentLinkingEntity.class);
                linkingEntities.add(documentLinkingEntity);
            }

            return linkingEntities;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentSummaryEntity extends ViewDocument {
        @JsonIgnore
        private EntityLinking.ResponseEntityLinking entityLinking;
        private EntityLinking.ResponseEntityLinkingSummary entityLinking_summary;

        public DocumentSummaryEntity(String _id, String title, String description, String content, String writer, String published_at, String source_uri, String lang, String search_type, Boolean internalsearch, String keyword, String serviceType, Float score, String og_image_url, String og_image_base64, String favicon_base64,String favicon_url,  EntityLinking.ResponseEntityLinkingSummary entityLinking_summary) {
            super(_id, title, description, content, writer, published_at, source_uri, lang, search_type, internalsearch, keyword, serviceType, score, og_image_url, og_image_base64, favicon_base64, favicon_url);
            this.entityLinking_summary = entityLinking_summary;
        }

        public List<DocumentSummaryEntity> covert(List<DocumentEntity> entities) throws JsonProcessingException {
            if (entities == null) return null;
            List<DocumentSummaryEntity> linkingEntities = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            for (DocumentEntity entity : entities) {
                DocumentSummaryEntity documentLinkingEntity = objectMapper.readValue(objectMapper.writeValueAsString(entity), DocumentSummaryEntity.class);
                linkingEntities.add(documentLinkingEntity);
            }

            return linkingEntities;
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentNoneEntity extends ViewDocument {
        @JsonIgnore
        private EntityLinking.ResponseEntityLinking entityLinking;
        @JsonIgnore
        private EntityLinking.ResponseEntityLinkingSummary entityLinking_summary;

        public DocumentNoneEntity(String _id, String title, String description, String content, String writer, String published_at, String source_uri, String lang, String search_type,  Boolean internalsearch, String keyword, String service_type, Float score,String og_image_url, String og_image_base64, String favicon_base64,String favicon_url,  EntityLinking.ResponseEntityLinking entityLinking, EntityLinking.ResponseEntityLinkingSummary entityLinking_summary) {
            super(_id, title, description, content, writer, published_at, source_uri, lang, search_type,internalsearch, keyword, service_type, score, og_image_url, og_image_base64, favicon_base64, favicon_url);
            this.entityLinking = entityLinking;
            this.entityLinking_summary = entityLinking_summary;
        }

        public List<DocumentNoneEntity> covert(List<DocumentEntity> entities) throws JsonProcessingException {
            if (entities == null) return null;
            List<DocumentNoneEntity> linkingEntities = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            for (DocumentEntity entity : entities) {
                DocumentNoneEntity documentLinkingEntity = objectMapper.readValue(objectMapper.writeValueAsString(entity), DocumentNoneEntity.class);
                linkingEntities.add(documentLinkingEntity);
            }

            return linkingEntities;
        }
    }

}
