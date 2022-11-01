package com.ds.dssearcher.entity;

import com.ds.dssearcher.util.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentRealtimeKeywordSearchEntity extends DocumentPeopleEntity{
    private String keyword;
    public List<DocumentRealtimeKeywordSearchEntity> covnertData(List<DocumentRealtimeSearchEntity> documentRealtimeCrawlerEntities, Integer type,String keyword){

        List<DocumentRealtimeKeywordSearchEntity> documentRealtimeKeywordSearchEntities = new ArrayList<>();;
        if(documentRealtimeCrawlerEntities != null && !documentRealtimeCrawlerEntities.isEmpty()){
            for(DocumentRealtimeSearchEntity documentRealtimeSearchEntity : documentRealtimeCrawlerEntities){
                DocumentRealtimeKeywordSearchEntity documentRealtimeKeywordSearchEntity = new DocumentRealtimeKeywordSearchEntity();
                documentRealtimeKeywordSearchEntity.setKeyword(keyword);
                documentRealtimeKeywordSearchEntity.setWriter(documentRealtimeSearchEntity.getScreen_name());
                documentRealtimeKeywordSearchEntity.setLang(documentRealtimeSearchEntity.getLang());
                String content =  documentRealtimeSearchEntity.getText();
                if(content != null ){
                    if(content.length() > 100){
                        String ct = content.substring(0,99);
                        String sb = null;
                        if(content.charAt(99) != '.' && content.charAt(99) != '?' && content.charAt(99) != '!'){
                            sb = content.substring(99).split("\\.|\\?|\\!")[0];
                        }
                        documentRealtimeKeywordSearchEntity.setDescription((sb != null ? ct + sb: ct ).replaceAll("\n|\r","").trim());
                    }else{
                        documentRealtimeKeywordSearchEntity.setDescription(content.replaceAll("\n|\r","").trim());
                    }
                }
                documentRealtimeKeywordSearchEntity.setContent( documentRealtimeSearchEntity.getText());
                if(type == 5){
                    documentRealtimeKeywordSearchEntity.setFavicon_base64(Constant.FaviconBase64.TWITTER);
                    documentRealtimeKeywordSearchEntity.setService_type("TWITTER");
                }else{

                }
                documentRealtimeKeywordSearchEntity.setPublished_at(documentRealtimeSearchEntity.getCreated_at_parsed());
                documentRealtimeKeywordSearchEntity.setWriter_id(documentRealtimeSearchEntity.getAuthor_id());
                documentRealtimeKeywordSearchEntity.setSource_uri("https://twitter.com/"+ documentRealtimeSearchEntity.getScreen_name()+"/status/"+ documentRealtimeSearchEntity.getId_str());
                documentRealtimeKeywordSearchEntities.add(documentRealtimeKeywordSearchEntity);
            }
        }
        return documentRealtimeKeywordSearchEntities;

    }
    public List<DocumentRealtimeKeywordSearchEntity> covert(List<DocumentPeopleEntity> documentPeopleEntities, String keyword){
        List<DocumentRealtimeKeywordSearchEntity> documentRealtimeKeywordSearchEntities = new ArrayList<>();
        if(documentPeopleEntities != null && !documentPeopleEntities.isEmpty()){
            documentPeopleEntities.forEach(documentPeopleEntity -> {
                DocumentRealtimeKeywordSearchEntity documentRealtimeKeywordSearchEntity = new DocumentRealtimeKeywordSearchEntity();
                documentRealtimeKeywordSearchEntity.setKeyword(keyword);
                documentRealtimeKeywordSearchEntity.set_id(documentPeopleEntity.get_id());
                documentRealtimeKeywordSearchEntity.setTitle(documentPeopleEntity.getTitle());
                documentRealtimeKeywordSearchEntity.setPublished_at(documentPeopleEntity.getPublished_at());
                documentRealtimeKeywordSearchEntity.setService_type(documentPeopleEntity.getService_type());
                documentRealtimeKeywordSearchEntity.setFavicon_base64(documentPeopleEntity.getFavicon_base64());
                documentRealtimeKeywordSearchEntity.setContent(documentPeopleEntity.getContent());
                documentRealtimeKeywordSearchEntity.setDescription(documentPeopleEntity.getDescription());
                documentRealtimeKeywordSearchEntity.setWriter(documentPeopleEntity.getWriter());
                documentRealtimeKeywordSearchEntity.setWriter_id(documentPeopleEntity.getWriter_id());
                documentRealtimeKeywordSearchEntity.setFavicon_base64(documentPeopleEntity.getFavicon_base64());
                documentRealtimeKeywordSearchEntity.setLang(documentPeopleEntity.getLang());
                documentRealtimeKeywordSearchEntity.setSource_uri(documentPeopleEntity.getSource_uri());
                documentRealtimeKeywordSearchEntity.setOg_image_url(documentPeopleEntity.getOg_image_url());
                documentRealtimeKeywordSearchEntities.add(documentRealtimeKeywordSearchEntity);
            });
        }
        return documentRealtimeKeywordSearchEntities;
    }
}
