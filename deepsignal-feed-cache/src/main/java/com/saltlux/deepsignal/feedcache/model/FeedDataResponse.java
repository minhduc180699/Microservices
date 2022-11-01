package com.saltlux.deepsignal.feedcache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedDataResponse {
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
//    private String comment_id;
    //    private String subcategory_url;
    private String collected_at;
    private String tags;
    //    private String content_search;
    private String service_type;
    private String origin_date;
//    private String reply_id;
    private String source_id;
    private String category;
    private String writer_search;
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
        this.docId = docModel.getDocId();
        this.type = docModel.getType();
        this.feed_content_id = docModel.getFeed_content_id();
        this.liked = docModel.getLiked();
        this.isBookmarked = docModel.getIsBookmarked();
        this.isRecommend = docModel.getIsRecommend();
        this.isDeleted = docModel.getIsDeleted();
        this.isFeed = docModel.getIsFeed();
        this.created_date = docModel.getCreated_date();
        this.created_by = docModel.getCreated_by();
        this.lang = docModel.getLang();
        this.keyword = docModel.getKeyword();
        this.search_type = docModel.getSearch_type();
        this.published_at = docModel.getPublished_at();
        this.channel = docModel.getChannel();
        this.collector = docModel.getCollector();
        this.title = docModel.getTitle();
        this.writer = docModel.getWriter();
        this.og_image_url = docModel.getOg_image_url();
        this.og_image_base64 = docModel.getOg_image_base64();
        this.favicon_url = docModel.getFavicon_url();
        this.favicon_base64 = docModel.getFavicon_base64();
//        this.web_source_category = feedContentModel.getWeb_source_category();
//        this.category_url = feedContentModel.getCategory_url();
        this.description = docContentModel.getDescription();
        this.source_uri = docContentModel.getSource_uri();
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
        this.feed_partition = docModel.getFeed_partition();
        this.content_partition = docModel.getContent_partition();
        this.url = docModel.getUrl();
    }
}

