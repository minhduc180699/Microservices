package com.ds.dssearcher.entity;


import com.ds.dssearcher.util.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentRealtimePeopleSearchEntity extends DocumentPeopleEntity{

    private String accountId;

    public List<DocumentRealtimePeopleSearchEntity> covnertData(List<DocumentRealtimeCrawlerEntity> documentRealtimeCrawlerEntities, Integer type){

        List<DocumentRealtimePeopleSearchEntity> documentRealtimePeopleSearchEntities = new ArrayList<>();;
        if(documentRealtimeCrawlerEntities != null && !documentRealtimeCrawlerEntities.isEmpty()){
            for(DocumentRealtimeCrawlerEntity documentRealtimeCrawlerEntity : documentRealtimeCrawlerEntities){
                DocumentRealtimePeopleSearchEntity documentRealtimePeopleSearchEntity = new DocumentRealtimePeopleSearchEntity();
                documentRealtimePeopleSearchEntity.setAccountId(documentRealtimeCrawlerEntity.getScreen_name());
                documentRealtimePeopleSearchEntity.setWriter(documentRealtimeCrawlerEntity.getScreen_name());
                documentRealtimePeopleSearchEntity.setLang(documentRealtimeCrawlerEntity.getLang());
                String content =  documentRealtimeCrawlerEntity.getText();
                if(content != null ){
                    if(content.length() > 100){
                        String ct = content.substring(0,99);
                        String sb = null;
                        if(content.charAt(99) != '.' && content.charAt(99) != '?' && content.charAt(99) != '!'){
                            sb = content.substring(99).split("\\.|\\?|\\!")[0];
                        }
                        documentRealtimePeopleSearchEntity.setDescription((sb != null ? ct + sb: ct ).replaceAll("\n|\r","").trim());
                    }else{
                        documentRealtimePeopleSearchEntity.setDescription(content.replaceAll("\n|\r","").trim());
                    }
                }
                documentRealtimePeopleSearchEntity.setContent( documentRealtimeCrawlerEntity.getText());
                if(type == 5){
                    documentRealtimePeopleSearchEntity.setFavicon_base64(Constant.FaviconBase64.TWITTER);
                    documentRealtimePeopleSearchEntity.setService_type("TWITTER");
                }else{

                }
                documentRealtimePeopleSearchEntity.setPublished_at(documentRealtimeCrawlerEntity.getCreated_at_parsed());
                documentRealtimePeopleSearchEntity.setWriter_id(documentRealtimeCrawlerEntity.getAuthor_id());
                documentRealtimePeopleSearchEntity.setSource_uri("https://twitter.com/"+ documentRealtimeCrawlerEntity.getScreen_name()+"/status/"+ documentRealtimeCrawlerEntity.getId_str());
                documentRealtimePeopleSearchEntities.add(documentRealtimePeopleSearchEntity);
            }
        }
        return documentRealtimePeopleSearchEntities;

    }

    public List<DocumentRealtimePeopleSearchEntity> covert(List<DocumentPeopleEntity> documentPeopleEntities,String accountId){
        List<DocumentRealtimePeopleSearchEntity> documentRealtimePeopleSearchEntities = new ArrayList<>();
        if(documentPeopleEntities != null && !documentPeopleEntities.isEmpty()){
            documentPeopleEntities.forEach(documentPeopleEntity -> {
                DocumentRealtimePeopleSearchEntity documentRealtimePeopleSearchEntity = new DocumentRealtimePeopleSearchEntity();
                documentRealtimePeopleSearchEntity.setAccountId(accountId);
                documentRealtimePeopleSearchEntity.set_id(documentPeopleEntity.get_id());
                documentRealtimePeopleSearchEntity.setTitle(documentPeopleEntity.getTitle());
                documentRealtimePeopleSearchEntity.setPublished_at(documentPeopleEntity.getPublished_at());
                documentRealtimePeopleSearchEntity.setService_type(documentPeopleEntity.getService_type());
                documentRealtimePeopleSearchEntity.setFavicon_base64(documentPeopleEntity.getFavicon_base64());
                documentRealtimePeopleSearchEntity.setContent(documentPeopleEntity.getContent());
                documentRealtimePeopleSearchEntity.setDescription(documentPeopleEntity.getDescription());
                documentRealtimePeopleSearchEntity.setWriter(documentPeopleEntity.getWriter());
                documentRealtimePeopleSearchEntity.setWriter_id(documentPeopleEntity.getWriter_id());
                documentRealtimePeopleSearchEntity.setFavicon_base64(documentPeopleEntity.getFavicon_base64());
                documentRealtimePeopleSearchEntity.setLang(documentPeopleEntity.getLang());
                documentRealtimePeopleSearchEntity.setSource_uri(documentPeopleEntity.getSource_uri());
                documentRealtimePeopleSearchEntity.setOg_image_url(documentPeopleEntity.getOg_image_url());
                documentRealtimePeopleSearchEntities.add(documentRealtimePeopleSearchEntity);
            });
        }
        return documentRealtimePeopleSearchEntities;
    }
}
