package com.saltlux.deepsignal.feedcache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocDataModel {
    // Feed properties
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String _id;
    private String connectomeId;
    private String requestId = "NULL";
    private String docId;
    private String type;
    private String url;
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
    private String keyword;
    private String search_type;
    private String published_at;
    private String channel;
    private String collector = "RECOMMENDATION";
    private String title;
    private String writer;
    private String og_image_url;
    private String og_image_base64;
    private String favicon_url;
    private String favicon_base64;
    // Feed content
//    private String web_source_category;
//    private String category_url;
    private String description;
    private String source_uri;
//    private String subdomain_url;
    private String content;
    private String feed_id;
    private String html_content;
    private String video_url;
    private List<String> image_base64;
//    private String domain_url;
//    private String project_id;
//    private String web_source_id;
    private String writer_id;
    private List<String> image_url;
    private String video_base64;
    private String comment_id;
//    private String subcategory_url;
    private String collected_at;
    private String tags;
//    private String content_search;
    private String service_type;
    private String origin_date;
    private String reply_id;
    private String source_id;
    private String category;
//    private String writer_search;
    @JsonIgnore
    private Long __unique__key__summary;
    @JsonIgnore
    private Long __unique__key;


    private String feed_partition;
    private String content_partition;


    private String lang ;
    public void buildFeedDataModel(DocModel docModel, DocContentModel docContentModel){
        this._id = docModel.get_id();
        this.connectomeId = docModel.getConnectomeId();
//        this.requestId = feedModel.getRequestId();
        this.docId = docContentModel.getDocId();
        this.type = docModel.getType();
        this.feed_content_id = docModel.getFeed_content_id();
        this.liked = docModel.getLiked();
        this.isBookmarked = docModel.getIsBookmarked();
        this.isRecommend = docModel.getIsRecommend();
        this.isDeleted = docModel.getIsDeleted();
        this.isFeed = docModel.getIsFeed();
        this.created_date = docModel.getCreated_date();
        this.created_by = docModel.getCreated_by();
        this.lang = docContentModel.getLang();
        this.keyword = docContentModel.getKeyword();
        this.search_type = docContentModel.getSearch_type();
        this.published_at = docContentModel.getPublished_at();
        this.channel = docModel.getChannel();
        this.collector = docContentModel.getCollector();
        this.title = docContentModel.getTitle();
        this.writer = docContentModel.getWriter();
        this.og_image_url = docContentModel.getOg_image_url();
        this.og_image_base64 = docContentModel.getOg_image_base64();
        this.favicon_url = docModel.getFavicon_url();
        this.favicon_base64 = docContentModel.getFavicon_base64();
//        this.web_source_category = feedContentModel.getWeb_source_category();
//        this.category_url = feedContentModel.getCategory_url();
        this.description = docContentModel.getDescription();
        this.source_uri = docContentModel.getUrl();
//        this.subdomain_url = feedContentModel.getSubdomain_url();
        this.content = docContentModel.getContent();
        this.feed_id = docContentModel.getFeed_id();
        this.html_content = docContentModel.getHtml_content();
        this.video_url = docContentModel.getVideo_url();
        this.image_base64 = docContentModel.getImage_base64();
//        this.domain_url = feedContentModel.getDomain_url();
//        this.project_id = feedContentModel.getProject_id();
//        this.web_source_id = feedContentModel.getWeb_source_id();
        this.writer_id = docContentModel.getWriter_id();
        this.image_url = docContentModel.getImage_url();
        this.video_base64 = docContentModel.getVideo_base64();
//        this.comment_id = feedContentModel.getComment_id();
//        this.subcategory_url = feedContentModel.getSubcategory_url();
        this.collected_at = docContentModel.getCollected_at();
        this.tags = docContentModel.getTags();
//        this.content_search = feedContentModel.getContent_search();
        this.service_type = docContentModel.getService_type();
        this.origin_date = docContentModel.getOrigin_date();
//        this.reply_id = feedContentModel.getReply_id();
        this.source_id = docContentModel.getSource_id();
        this.category = docContentModel.getCategory();
//        this.writer_search = docContentModel.getWriter_search();
        this.feed_partition = docModel.getFeed_partition();
        this.content_partition = docContentModel.getContent_partition();
        this.url = docModel.getUrl();
    }
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
        feedRealtimeCrawlerModel.setPath(this.source_uri);

        feedRealtimeCrawlerModel.setDescription(this.description);
        feedRealtimeCrawlerModel.setCreatedDate(this.published_at);
        feedRealtimeCrawlerModel.setAuthor(this.writer);
        feedRealtimeCrawlerModel.setFavicon(this.favicon_url);
        feedRealtimeCrawlerModel.setLang(this.lang);
        feedRealtimeCrawlerModel.setKeyword(this.keyword);
        return feedRealtimeCrawlerModel;
    }

    public void mergeDocData(DocDataModel docDataModel){
        if (docDataModel == null){
            return ;
        }
        if(docDataModel.type != null){
            this.type = docDataModel.type;
        }
        if(docDataModel.url != null){
            this.url = docDataModel.url;
        }
        if(docDataModel.feed_content_id != null){
            this.feed_content_id = docDataModel.feed_content_id;
        }
        if(docDataModel.liked != null){
            this.liked = docDataModel.liked;
        }
        if(docDataModel.isBookmarked != null){
            this.isBookmarked = docDataModel.isBookmarked;
        }
        if(docDataModel.isRecommend != null){
            this.isRecommend = docDataModel.isRecommend;
        }
        if(docDataModel.isDeleted != null){
            this.isDeleted = docDataModel.isDeleted;
        }
        if(docDataModel.isFeed != null){
            this.isFeed = docDataModel.isFeed;
        }
        if(docDataModel.created_date != null){
            this.created_date = docDataModel.created_date;
        }
        if(docDataModel.created_by != null){
            this.created_by = docDataModel.created_by;
        }
        if(docDataModel.lang != null){
            this.lang = docDataModel.lang;
        }
        if(docDataModel.keyword != null){
            this.keyword = docDataModel.keyword;
        }
        if(docDataModel.search_type != null){
            this.search_type = docDataModel.search_type;
        }
        if(docDataModel.published_at != null){
            this.published_at = docDataModel.published_at;
        }
        if(docDataModel.channel != null){
            this.channel = docDataModel.channel;
        }
        if(docDataModel.collector != null){
            this.collector = docDataModel.collector;
        }
        if(docDataModel.title != null){
            this.title = docDataModel.title;
        }
        if(docDataModel.writer != null){
            this.writer = docDataModel.writer;
        }
        if(docDataModel.og_image_url != null){
            this.og_image_url = docDataModel.og_image_url;
        }
        if(docDataModel.og_image_base64 != null){
            this.og_image_base64 = docDataModel.og_image_base64;
        }
        if(docDataModel.favicon_url != null){
            this.favicon_url = docDataModel.favicon_url;
        }
        if(docDataModel.favicon_base64 != null){
            this.favicon_base64 = docDataModel.favicon_base64;
        }
        if(docDataModel.description != null){
            this.description = docDataModel.description;
        }
        if(docDataModel.feed_partition != null){
            this.feed_partition = docDataModel.feed_partition;
        }
        if(docDataModel.content_partition != null){
            this.content_partition = docDataModel.content_partition;
        }
        if(docDataModel.tags != null){
            this.tags = docDataModel.tags;
        }
        if(docDataModel.feed_id != null){
            this.feed_id = docDataModel.feed_id;
        }
        if(docDataModel.writer_id != null){
            this.writer_id = docDataModel.writer_id;
        }
        if (docDataModel.source_uri != null) {
            this.source_uri = docDataModel.source_uri;
        }
        if (docDataModel.content != null) {
            this.content = docDataModel.content;
        }
        if (docDataModel.html_content != null) {
            this.html_content = docDataModel.html_content;
        }
        if (docDataModel.video_url != null) {
            this.video_url = docDataModel.video_url;
        }
        if (docDataModel.image_base64 != null) {
            this.image_base64 = docDataModel.image_base64;
        }
        if (docDataModel.image_url != null) {
            this.image_url = docDataModel.image_url;
        }
        if (docDataModel.video_base64 != null) {
            this.video_base64 = docDataModel.video_base64;
        }
        if (docDataModel.collected_at != null) {
            this.collected_at = docDataModel.collected_at;
        }
        if (docDataModel.service_type != null) {
            this.service_type = docDataModel.service_type;
        }
        if (docDataModel.origin_date != null) {
            this.origin_date = docDataModel.origin_date;
        }
        if (docDataModel.source_id != null) {
            this.source_id = docDataModel.source_id;
        }
        if (docDataModel.category != null) {
            this.category = docDataModel.category;
        }
    }
}
