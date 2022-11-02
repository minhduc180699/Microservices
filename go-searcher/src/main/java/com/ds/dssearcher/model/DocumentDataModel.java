package com.ds.dssearcher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentDataModel {
    // Feed properties
    private String _id;
    private String connectomeId;
    private String recommend_date;
    private String docId;
    private String recommendation_type;
    private String type;
    private String feed_content_id;
    private Integer liked;
    private Boolean isBookmarked;
    private Boolean isRecommend;
    private Boolean isDeleted;
    private Boolean isFeed;
    private String created_date;
    private String created_by;
    //    private String service_language;
    private String lang;
    private String keyword;
    private String search_type;
    private String published_at;
    private String channel;
    private String collector;
    private String title;
    private String writer;

    //    private String og_image;
//    private String favicon;
    private String og_image_url;
    private String og_image_base64;
    private String favicon_url;
    private String favicon_base64;
    // Feed content
    private String web_source_category;
    private String category_url;
    private String description;
    private String url;
    private String subdomain_url;
    private String content;
    private String feed_id;
    private String html_content;
    private String video_url;
    private List<Object> image_base64;
    private String domain_url;
    private String project_id;
    private String web_source_id;
    private String writer_id;
    private List<Object> image_url;
    private String video_base64;
    private String comment_id;
    private String subcategory_url;
    private String collected_at;
    private String tags;
    private String content_search;
    private String service_type;
    private String origin_date;
    private String reply_id;
    private String source_id;
    private String category;
    private Long __unique__key__summary;
    private Long __unique__key;

    public DocumentDataModel(DocumentModel feedModel, DocContentModel docContentModel) {
        this._id = feedModel.get_id();
        this.connectomeId = feedModel.getConnectomeId();
        this.recommend_date = feedModel.getRecommend_date();
        this.docId = feedModel.getDocId();
        this.recommendation_type = feedModel.getRecommendation_type();
        this.type = feedModel.getType();
        this.feed_content_id = feedModel.getFeed_content_id();
        this.liked = feedModel.getLiked();
        this.isBookmarked = feedModel.getIsBookmarked();
        this.isRecommend = feedModel.getIsRecommend();
        this.isDeleted = feedModel.getIsDeleted();
        this.isFeed = feedModel.getIsFeed();
        this.created_date = feedModel.getCreated_date();
        this.created_by = feedModel.getCreated_by();
        this.lang = feedModel.getLang();
        this.keyword = feedModel.getKeyword();
        this.search_type = feedModel.getSearch_type();
        this.published_at = feedModel.getPublished_at();
        this.channel = feedModel.getChannel();
        this.collector = feedModel.getCollector();
        this.title = feedModel.getTitle();
        this.writer = feedModel.getWriter();
//        this.og_image = feedModel.getOg_image();
//        this.favicon = feedModel.getFavicon();
        this.og_image_url = feedModel.getOg_image_url();
        this.og_image_base64 = feedModel.getOg_image_base64();
        this.favicon_url = feedModel.getFavicon_url();
        this.favicon_base64 = feedModel.getFavicon_base64();
//        this.web_source_category = feedContentModel.getWeb_source_category();
//        this.category_url = feedContentModel.getCategory_url();
        this.description = docContentModel.getDescription();
        this.url = docContentModel.getUrl();
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
    }
}

