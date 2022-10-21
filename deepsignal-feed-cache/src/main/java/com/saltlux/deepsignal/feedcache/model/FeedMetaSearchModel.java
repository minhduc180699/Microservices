package com.saltlux.deepsignal.feedcache.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedMetaSearchModel {
    private String connectomeId;
    private Long id;
    private String title;
    private String description;
    private String favicon_url;
    private String favicon_base64;
    private String source_uri;
    private String writer;
    private String search_type;
    private String __unique__key;
    private String keyword;
    private String og_image_url;
    private String og_image_base64;
    private String service_language;
    private String published_at;
    private String created_by = "SYSTEM";
    private String requestBy = "RECOMMEND";
    private String channel;
    private String collector;
    private String tags;


    public FeedRealtimeCrawlerModel toFeedRealtimeCrawlerModel(){
        FeedRealtimeCrawlerModel feedRealtimeCrawlerModel = new FeedRealtimeCrawlerModel();
        String[] searchTypeArray = this.search_type.split(":");
        if (searchTypeArray[0].toLowerCase().equalsIgnoreCase("searchFileType")){
            feedRealtimeCrawlerModel.setType("FILE");
        }else if (searchTypeArray[0].equalsIgnoreCase("searchNews")){
            feedRealtimeCrawlerModel.setType("URL");
        }

        feedRealtimeCrawlerModel.setId(this.id);
        feedRealtimeCrawlerModel.setConnectomeId(this.connectomeId);
        feedRealtimeCrawlerModel.setName(this.title);
        feedRealtimeCrawlerModel.setPath(this.source_uri);
        feedRealtimeCrawlerModel.setFileType(searchTypeArray[1]);
        feedRealtimeCrawlerModel.setDescription(this.description);
        feedRealtimeCrawlerModel.setCreatedDate(this.published_at);
        feedRealtimeCrawlerModel.setAuthor(this.writer);
        feedRealtimeCrawlerModel.setSearchType(this.search_type);
        feedRealtimeCrawlerModel.setFavicon(this.favicon_url);
        feedRealtimeCrawlerModel.setImg(this.favicon_base64);
        feedRealtimeCrawlerModel.setLang(this.service_language);
        feedRealtimeCrawlerModel.setKeyword(this.keyword);
        feedRealtimeCrawlerModel.setRequestBy(this.requestBy);
        return feedRealtimeCrawlerModel;
    }
    public FeedDataModel toFeedDataModel(){
        FeedDataModel feedDataModel = new FeedDataModel();
        feedDataModel.setTitle(this.title);
        feedDataModel.setDescription(this.description);
        feedDataModel.setFavicon_url(this.favicon_url);
        feedDataModel.setWriter(this.writer);
        feedDataModel.setKeyword(this.keyword);
        feedDataModel.setCreated_date(this.published_at);
        feedDataModel.setLang(this.getService_language());
        feedDataModel.setConnectomeId(this.connectomeId);
//        feedDataModel
        return feedDataModel;
    }
}
