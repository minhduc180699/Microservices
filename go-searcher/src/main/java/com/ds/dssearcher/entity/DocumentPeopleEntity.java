package com.ds.dssearcher.entity;

import com.ds.dssearcher.util.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.elasticsearch.search.SearchHit;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentPeopleEntity {

    private String _id;
    private String title;
    private String description;
    private String content;
    private String writer;
    private String writer_id;
    private String published_at;
    private String source_uri;
    private String lang;
    private String og_image_url;
    private String og_image_base64;
    private String favicon_url;
    private String favicon_base64;
    private String service_type;

    public DocumentPeopleEntity convertObjectToDoc(SearchHit hit){
        try{
            if(hit.getId() == null ) return null;
            DocumentPeopleEntity doc = new DocumentPeopleEntity();
            doc.set_id(hit.getId());
            if(hit.getFields().get(Constant.DocumentFieldName.TITLE_FIELD) != null)
                doc.setTitle(hit.getFields().get(Constant.DocumentFieldName.TITLE_FIELD).getValue());
            if(hit.getFields().get(Constant.DocumentFieldName.CONTENT_FIELD) != null || hit.getFields().get(Constant.DocumentFieldName.MESSAGE_FIELD) != null){
                String content = null;
                String fullContent = hit.getFields().get(Constant.DocumentFieldName.CONTENT_FIELD) != null ? hit.getFields().get(Constant.DocumentFieldName.CONTENT_FIELD).getValue()
                        : hit.getFields().get(Constant.DocumentFieldName.MESSAGE_FIELD).getValue();
                if(hit.getFields().get(Constant.DocumentFieldName.CONTENT_FIELD) != null){
                    content = hit.getFields().get(Constant.DocumentFieldName.CONTENT_FIELD).getValue();

                }else if(hit.getFields().get(Constant.DocumentFieldName.MESSAGE_FIELD) != null){
                    content = hit.getFields().get(Constant.DocumentFieldName.MESSAGE_FIELD).getValue();
                }

                if(content != null && content.length() > 100){
                    String ct = content.substring(0,99);
                    String sb = null;
                    if(content.charAt(99) != '.' && content.charAt(99) != '?' && content.charAt(99) != '!'){
                        sb = content.substring(99).split("\\.|\\?|\\!")[0];
                    }
                    doc.setDescription((sb != null ? ct + sb: ct ).replaceAll("\n|\r","").trim());
                }else{
                    doc.setDescription(content.replaceAll("\n|\r","").trim());
                }
                doc.setContent(fullContent);
            }
            if(hit.getFields().get(Constant.DocumentFieldName.AUTHOR_FIELD) != null){
                doc.setWriter(hit.getFields().get(Constant.DocumentFieldName.AUTHOR_FIELD).getValue());
            }

            if(hit.getFields().get(Constant.DocumentFieldName.SOURCE_FIELD) != null)
                doc.setSource_uri(hit.getFields().get(Constant.DocumentFieldName.SOURCE_FIELD).getValue());


            if(hit.getFields().get(Constant.DocumentFieldName.LANGUAGE_FIELD) != null)
                doc.setLang(LanguageEnum.getLang(hit.getFields().get(Constant.DocumentFieldName.LANGUAGE_FIELD).getValue()));
            if(hit.getFields().get(Constant.DocumentFieldName.PUBLISHED_FIELD) != null)
                doc.setPublished_at(hit.getFields().get(Constant.DocumentFieldName.PUBLISHED_FIELD).getValue());

            if(hit.getFields().get(Constant.DocumentFieldName.FAVICON_URL_FIELD) != null){
                doc.setFavicon_url(hit.getFields().get(Constant.DocumentFieldName.FAVICON_URL_FIELD).getValue());
            }
            if(hit.getFields().get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD) != null){
                doc.setFavicon_base64(hit.getFields().get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD).getValue());
            }else{
                if(hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD) != null){
                    String serviceType = hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD).getValue();
                    if (serviceType.equalsIgnoreCase("Twitter")){
                        doc.setFavicon_base64(Constant.FaviconBase64.TWITTER);
                    }else if (serviceType.equalsIgnoreCase("Facebook")){
                        doc.setFavicon_base64(Constant.FaviconBase64.FACEBOOK);
                    }else if(serviceType.equalsIgnoreCase("instagram")){
                        doc.setFavicon_base64(Constant.FaviconBase64.INSTAGRAM);
                    }
                }
            }

            if(hit.getFields().get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD) != null)
                doc.setOg_image_url(hit.getFields().get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD).getValue());
            if(hit.getFields().get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD) != null)
                doc.setOg_image_base64(hit.getFields().get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD).getValue());
            if(hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD) != null)
                doc.setService_type(hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD).getValue());

            if(hit.getFields().get(Constant.DocumentFieldName.WRITER_ID_FIELD) != null)
                doc.setWriter_id(hit.getFields().get(Constant.DocumentFieldName.WRITER_ID_FIELD).getValue());
            return doc;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
