package com.saltlux.deepsignal.feedcache.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocCreateModel {

    private String requestId = "NULL";
    private String connectomeId;
    private String created_by = "SYSTEM";
    @NotEmpty
    private String lang ;
    private String keyword;
    @NotEmpty
    private String search_type;
    private String published_at;

    @NotEmpty
    private String channel;
    private String collector = "RE";
    private String title;
    private String description;
    private String writer;
    private String og_image_url;
    private String og_image_base64;
    private String favicon_url;
    private String favicon_base64;
    private String tags;
    private String comment_id;
    private String url;
    private String content;
    private String content_id;
    private String reply_id;
    private String video_url;
    private String video_base64;
    private String html_content;
    private String collected_at;
    private String originDate;

    private Long __unique__key;

    public FeedRealtimeCrawlerModel toFeedRealtimeCrawlerModel(){
        FeedRealtimeCrawlerModel feedRealtimeCrawlerModel = new FeedRealtimeCrawlerModel();

        if (this.search_type.contains("searchFileType:")) {
            String[] searchTypeArray = this.search_type.split(":");
            feedRealtimeCrawlerModel.setType("DOWNLOAD");
            feedRealtimeCrawlerModel.setFileType(searchTypeArray[1]);
        }else {
            if (this.search_type.contains("VIDEO")) {
                feedRealtimeCrawlerModel.setSearchType("searchVideo");
            }
            if (this.search_type.contains("NEWS")) {
                feedRealtimeCrawlerModel.setSearchType("searchNews");
            }
            feedRealtimeCrawlerModel.setType("URL");
        }
        feedRealtimeCrawlerModel.setConnectomeId(this.connectomeId);
        feedRealtimeCrawlerModel.setName(this.title);
        feedRealtimeCrawlerModel.setPath(this.url);

        feedRealtimeCrawlerModel.setDescription(this.description);
        feedRealtimeCrawlerModel.setCreatedDate(this.published_at);
        feedRealtimeCrawlerModel.setAuthor(this.writer);
        feedRealtimeCrawlerModel.setFavicon(this.favicon_url);
        feedRealtimeCrawlerModel.setLang(this.lang);
        feedRealtimeCrawlerModel.setKeyword(this.keyword);
        return feedRealtimeCrawlerModel;
    }
}

