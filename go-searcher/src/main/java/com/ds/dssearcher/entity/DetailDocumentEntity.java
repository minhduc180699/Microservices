package com.ds.dssearcher.entity;

import com.ds.dssearcher.util.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.elasticsearch.search.SearchHit;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailDocumentEntity {

    private String _id;
    private String connectomeId;
    private String title;
    private String content;
    private String writer;
    private String published_at;
    private String source_uri;
    private String service_type;
    private String lang;
    private String og_image_url;
    private String og_image_base64;
    private String videos;
    private String favicon_url;
    private String favicon_base64;
    private List<String> image_url;
    private List<String> image_base64;

    public DetailDocumentEntity covertObjectToDoc(SearchHit hit) {

        try {
            if(hit.getId() == null) return null;
            DetailDocumentEntity doc = new DetailDocumentEntity();
            doc.set_id(hit.getId());
            doc.setConnectomeId(hit.getIndex());
            if (hit.getFields().get(Constant.DocumentFieldName.TITLE_FIELD) != null)
                doc.setTitle(hit.getFields().get(Constant.DocumentFieldName.TITLE_FIELD).getValue());
            if (hit.getFields().get(Constant.DocumentFieldName.CONTENT_FIELD) != null || hit.getFields().get(Constant.DocumentFieldName.MESSAGE_FIELD) != null) {
                doc.setContent(hit.getFields().get(Constant.DocumentFieldName.CONTENT_FIELD) != null ?
                        hit.getFields().get(Constant.DocumentFieldName.CONTENT_FIELD).getValue() :
                        hit.getFields().get(Constant.DocumentFieldName.MESSAGE_FIELD).getValue());
            }
            if (hit.getFields().get(Constant.DocumentFieldName.AUTHOR_FIELD) != null)
                doc.setWriter(hit.getFields().get(Constant.DocumentFieldName.AUTHOR_FIELD).getValue());
            if (hit.getFields().get(Constant.DocumentFieldName.SOURCE_FIELD) != null)
                doc.setSource_uri(hit.getFields().get(Constant.DocumentFieldName.SOURCE_FIELD).getValue());
            if (hit.getFields().get(Constant.DocumentFieldName.LANGUAGE_FIELD) != null)
                doc.setLang(hit.getFields().get(Constant.DocumentFieldName.LANGUAGE_FIELD).getValue());
            if (hit.getFields().get(Constant.DocumentFieldName.PUBLISHED_FIELD) != null)
                doc.setPublished_at(hit.getFields().get(Constant.DocumentFieldName.PUBLISHED_FIELD).getValue());
            if (hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD) != null)
                doc.setService_type(hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD).getValue());
            if(hit.getFields().get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD) != null)
                doc.setOg_image_url(hit.getFields().get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD).getValue());
            if(hit.getFields().get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD) != null)
                doc.setOg_image_base64(hit.getFields().get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD).getValue());

            if(hit.getFields().get(Constant.DocumentFieldName.FAVICON_URL_FIELD) != null)
                doc.setFavicon_url(hit.getFields().get(Constant.DocumentFieldName.FAVICON_URL_FIELD).getValue());
            if(hit.getFields().get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD) != null)
                doc.setFavicon_base64(hit.getFields().get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD).getValue());

            if (hit.getFields().get(Constant.DocumentFieldName.VIDEO_FIELD) != null)
                doc.setVideos(hit.getFields().get(Constant.DocumentFieldName.VIDEO_FIELD).getValue());

            if(hit.getFields().get(Constant.DocumentFieldName.IMAGE_URL_FIELD) != null)
                doc.setImage_url(hit.getFields().get(Constant.DocumentFieldName.IMAGE_URL_FIELD).getValues().stream()
                        .map(object -> Objects.toString(object, null))
                        .collect(Collectors.toList()));
            if(hit.getFields().get(Constant.DocumentFieldName.IMAGE_BASE64_FIELD) != null)
                doc.setImage_base64(hit.getFields().get(Constant.DocumentFieldName.IMAGE_BASE64_FIELD).getValues().stream()
                        .map(object -> Objects.toString(object, null))
                        .collect(Collectors.toList()));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
