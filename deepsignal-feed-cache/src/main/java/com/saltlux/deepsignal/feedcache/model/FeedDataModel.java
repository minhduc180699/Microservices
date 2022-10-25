package com.saltlux.deepsignal.feedcache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedDataModel {
    // Feed properties
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String _id;
    private String connectomeId;
    private String requestId = "NULL";
    private String docId_content;
    private String type;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String feed_content_id;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer liked;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isBookmarked;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isRecommend;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isDeleted;

    private Boolean isFeed;
    private String created_date;
    private String created_by = "SYSTEM";
    private String service_language;
    private String keyword;
    private String search_type;
    private String published_at;
    private String channel;
    private String collector;
    private String title;
    private String writer;
    private String og_image_url;
    private String og_image_base64;
    private String favicon_url;
    private String favicon_base64;
    // Feed content
    private String web_source_category;
    private String category_url;
    private String description;
    private String source_uri;
    private String subdomain_url;
    private String content;
    private String feed_id;
    private String html_content;
    private String video_url;
    private List<String> image_base64;
    private String domain_url;
    private String project_id;
    private String web_source_id;
    private String writer_id;
    private List<String> image_url;
    private String video_base64;
    private String comment_id = "0";
    private String subcategory_url;
    private String collected_at;
    private String tags;
    private String content_search;
    private String service_type;
    private String origin_date;
    private String reply_id = "0";
    private String source_id;
    private String category;
    private String writer_search;
    @JsonIgnore
    private Long __unique__key__summary;
    @JsonIgnore
    private Long __unique__key;
    private String lang = "en";
    public void buildFeedDataModel(FeedModel feedModel, FeedContentModel feedContentModel){
        this._id = feedModel.get_id();
        this.connectomeId = feedModel.getConnectomeId();
        this.requestId = feedModel.getRequestId();
        this.docId_content = feedModel.getDocId_content();
        this.type = feedModel.getType();
        this.feed_content_id = feedModel.getFeed_content_id();
        this.liked = feedModel.getLiked();
        this.isBookmarked = feedModel.getIsBookmarked();
        this.isRecommend = feedModel.getIsRecommend();
        this.isDeleted = feedModel.getIsDeleted();
        this.isFeed = feedModel.getIsFeed();
        this.created_date = feedModel.getCreated_date();
        this.created_by = feedModel.getCreated_by();
        this.service_language = feedModel.getService_language();
        this.keyword = feedModel.getKeyword();
        this.search_type = feedModel.getSearch_type();
        this.published_at = feedModel.getPublished_at();
        this.channel = feedModel.getChannel();
        this.collector = feedModel.getCollector();
        this.title = feedModel.getTitle();
        this.writer = feedModel.getWriter();
        this.og_image_url = feedModel.getOg_image_url();
        this.og_image_base64 = feedModel.getOg_image_base64();
        this.favicon_url = feedModel.getFavicon_url();
        this.favicon_base64 = feedModel.getFavicon_base64();
        this.web_source_category = feedModel.getFeed_content_id();
        this.category_url = feedModel.getFavicon_url();
        this.description = feedContentModel.getDescription();
        this.source_uri = feedContentModel.getSource_uri();
        this.subdomain_url = feedContentModel.getSubdomain_url();
        this.content = feedContentModel.getContent();
        this.feed_id = feedContentModel.getFeed_id();
        this.html_content = feedContentModel.getHtml_content();
        this.video_url = feedContentModel.getVideo_url();
        this.image_base64 = feedContentModel.getImage_base64();
        this.domain_url = feedContentModel.getDomain_url();
        this.project_id = feedContentModel.getProject_id();
        this.web_source_id = feedContentModel.getWeb_source_id();
        this.writer_id = feedContentModel.getWriter_id();
        this.image_url = feedContentModel.getImage_url();
        this.video_base64 = feedContentModel.getVideo_base64();
        this.comment_id = feedContentModel.getComment_id();
        this.subcategory_url = feedContentModel.getSubcategory_url();
        this.collected_at = feedContentModel.getCollected_at();
        this.tags = feedContentModel.getTags();
        this.content_search = feedContentModel.getContent_search();
        this.service_type = feedContentModel.getService_type();
        this.origin_date = feedContentModel.getOrigin_date();
        this.reply_id = feedContentModel.getReply_id();
        this.source_id = feedContentModel.getSource_id();
        this.category = feedContentModel.getCategory();
        this.writer_search = feedContentModel.getWriter_search();
    }
    public FeedRealtimeCrawlerModel toFeedRealtimeCrawlerModel(){
        FeedRealtimeCrawlerModel feedRealtimeCrawlerModel = new FeedRealtimeCrawlerModel();

        if (this.search_type.contains("searchFileType:")) {
            String[] searchTypeArray = this.search_type.split(":");
            feedRealtimeCrawlerModel.setType("FILE");
            feedRealtimeCrawlerModel.setFileType(searchTypeArray[1]);
        }else {
            feedRealtimeCrawlerModel.setType("URL");
        }
        feedRealtimeCrawlerModel.setConnectomeId(this.connectomeId);
        feedRealtimeCrawlerModel.setName(this.title);
        feedRealtimeCrawlerModel.setPath(this.source_uri);

        feedRealtimeCrawlerModel.setDescription(this.description);
        feedRealtimeCrawlerModel.setCreatedDate(this.published_at);
        feedRealtimeCrawlerModel.setAuthor(this.writer);
        feedRealtimeCrawlerModel.setSearchType(this.search_type);
        feedRealtimeCrawlerModel.setFavicon(this.favicon_url);
        feedRealtimeCrawlerModel.setLang(this.service_language);
        feedRealtimeCrawlerModel.setKeyword(this.keyword);
        return feedRealtimeCrawlerModel;
    }
}
