package com.ds.dssearcher.entity;

import com.ds.dssearcher.util.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.elasticsearch.search.SearchHit;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentSourceDataEntity {
        private String _id;
        private String title;
        private String description;
        private String content;
        private String writer;
        private String published_at;
        private String source_uri;
        private String lang;
        private String og_image_url;
        private String og_image_base64;
        private String favicon_url;
        private String favicon_base64;
        private Boolean internalsearch;
        private String service_type;

        public DocumentSourceDataEntity covertObjectToDoc(SearchHit hit){
        try{
            if(hit.getId() == null ) return null;
            DocumentSourceDataEntity doc = new DocumentSourceDataEntity();
            doc.set_id(hit.getId());
            doc.setInternalsearch(true);
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
                String type = hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD) != null ? hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD).getValue() : null;
                if(type != null && !type.isEmpty()){
                    if(type.equalsIgnoreCase("File:<doc>")){
                        doc.setFavicon_base64(Constant.FaviconBase64.DOC);
                    }else  if(type.equalsIgnoreCase("File:<pdf>")){
                        doc.setFavicon_base64(Constant.FaviconBase64.PDF);
                    }else  if(type.equalsIgnoreCase("File:<ppt>")){
                        doc.setFavicon_base64(Constant.FaviconBase64.PPT);
                    }else if(type.equalsIgnoreCase("Dart")){
                        doc.setFavicon_base64(Constant.FaviconBase64.DART);
                    }else if (type.equalsIgnoreCase("Investing")){
                        doc.setFavicon_base64(Constant.FaviconBase64.INVESTING);
                    }else if (type.equalsIgnoreCase("Linkedin")){
                        doc.setFavicon_base64(Constant.FaviconBase64.LINKEDIN);
                    }else if (type.equalsIgnoreCase("Twitter")){
                        doc.setFavicon_base64(Constant.FaviconBase64.TWITTER);
                    }else if (type.equalsIgnoreCase("Facebook")){
                        doc.setFavicon_base64(Constant.FaviconBase64.FACEBOOK);
                    }else if(type.equalsIgnoreCase("youtube")){
                        doc.setFavicon_base64(Constant.FaviconBase64.YOUTUBE);
                    }else if(type.equalsIgnoreCase("instagram")){
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

            return doc;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
