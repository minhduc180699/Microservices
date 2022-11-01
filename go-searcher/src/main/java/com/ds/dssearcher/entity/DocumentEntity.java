package com.ds.dssearcher.entity;

import com.ds.dssearcher.util.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.elasticsearch.search.SearchHit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentEntity {

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
    private String favicon_base64;
    private String favicon_url;
    private String search_type;
    private Boolean internalsearch;
    private String keyword;
    private String service_type;
    private Float score;
    private EntityLinking.ResponseEntityLinking entityLinking;
    private EntityLinking.ResponseEntityLinkingSummary entityLinking_summary;
    public DocumentEntity covertObjectToDoc(SearchHit hit,String searchType,String keyword,String type){
        try{
            if(hit.getId() == null ) return null;
            DocumentEntity doc = new DocumentEntity();
            doc.set_id(hit.getId());
            doc.setInternalsearch(true);
            doc.setKeyword(keyword);
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
            if(searchType != null && !searchType.isEmpty()){

                if(type.equalsIgnoreCase("File:<doc>")){
                    doc.setSearch_type("File:<doc>");
                }else  if(type.equalsIgnoreCase("File:<pdf>")){
                    doc.setSearch_type("File:<pdf>");
                }else  if(type.equalsIgnoreCase("File:<ppt>")){
                    doc.setSearch_type("File:<ppt>");
                }else if(type.equalsIgnoreCase("Dart")){
                    doc.setSearch_type("Social");
                }else if (type.equalsIgnoreCase("Investing")){
                    doc.setSearch_type("Social");
                }else if (type.equalsIgnoreCase("Linkedin")){
                    doc.setSearch_type("Social");
                }else if (type.equalsIgnoreCase("Twitter")){
                    doc.setSearch_type("Social");
                }else if (type.equalsIgnoreCase("Facebook")){
                    doc.setSearch_type("Social");
                }else if (type.equalsIgnoreCase("Youtube")){
                    doc.setSearch_type("Social");
                }else if (type.equalsIgnoreCase("Blog")){
                    doc.setSearch_type("Social");
                }else if (type.equalsIgnoreCase("Instagram")){
                    doc.setSearch_type("Social");
                }else if(type.equalsIgnoreCase("Video")){
                    doc.setSearch_type("Video");
                }else if(type.equalsIgnoreCase("News")){
                    doc.setSearch_type("News");
                }
            }

            if(hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD) != null)
                doc.setService_type(hit.getFields().get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD).getValue());

                doc.setScore(hit.getScore());

//            if(hit.getFields().get(Constant.DocumentFieldName.ENTITY_LINKING_FIELD) != null)
//                doc.setEntityLinking(hit.getFields().get(Constant.DocumentFieldName.ENTITY_LINKING_FIELD).getValue().toString().replaceAll("\\\\",""));
            return doc;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
