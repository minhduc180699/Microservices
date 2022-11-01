package com.ds.dssearcher.model;

import com.ds.dssearcher.util.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedModel {

    private String _id;
    private String connectomeId;
    private String docId;
    private String type;
    private String url;
    private String feed_content_id;
    private Boolean isBookmarked;
    private Integer liked;
    private Boolean isDeleted;
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
    private String writer_id;
    private String writer_search;
//    private String og_image;
    private String og_image_url;
    private String og_image_base64;
//    private String favicon;
    private String favicon_url;
    private String favicon_base64;
    private String feed_partition;
    private String feed_id;
    private String content_partition;
    private String tags;

    private String description;
    private String recommend_date;
    private String recommendation_type;
    private Boolean isRecommend;
    private Boolean isFeed;

    private static final Logger logger = LoggerFactory.getLogger(FeedModel.class);

    public static FeedModel convertToFeedDocument(SearchHit hit) {

        if (hit.getId() == null) {
            return null;
        }

        FeedModel feedModel = new FeedModel();
        Map<String, DocumentField> response = hit.getFields();

        try {
            if (hit.getId() != null) {
                feedModel.set_id(hit.getId());
            }
            if (response.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD) != null) {
                feedModel.setConnectomeId(response.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.FEED_CONTENT_ID_FIELD) != null) {
                feedModel.setFeed_content_id(response.get(Constant.DocumentFieldName.FEED_CONTENT_ID_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.FEED_CONTENT_ID_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD) != null) {
                feedModel.setDocId(response.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.LIKED_FIELD) != null) {
                feedModel.setLiked(Integer.parseInt(response.get(Constant.DocumentFieldName.LIKED_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.LIKED_FIELD).getValues().toString().length()-1)));
            }
            if (response.get(Constant.DocumentFieldName.TYPE_FEED_FIELD) != null) {
                feedModel.setType(response.get(Constant.DocumentFieldName.TYPE_FEED_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.TYPE_FEED_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.IS_DELETED_FIELD) != null) {
                feedModel.setIsDeleted(response.get(Constant.DocumentFieldName.IS_DELETED_FIELD).getValue().toString().equalsIgnoreCase("true"));
            }
            if (response.get(Constant.DocumentFieldName.CREATED_DATE_FIELD) != null) {
                feedModel.setCreated_date(response.get(Constant.DocumentFieldName.CREATED_DATE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CREATED_DATE_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.CREATED_BY_FIELD) != null) {
                feedModel.setCreated_by(response.get(Constant.DocumentFieldName.CREATED_BY_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CREATED_BY_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.LANGUAGE_FIELD) != null) {
                feedModel.setLang(languageToLang(response.get(Constant.DocumentFieldName.LANGUAGE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.LANGUAGE_FIELD).getValues().toString().length()-1)));
            }
            if (response.get(Constant.DocumentFieldName.KEYWORD_FIELD) != null) {
                feedModel.setKeyword(response.get(Constant.DocumentFieldName.KEYWORD_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.KEYWORD_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.SEARCH_TYPE) != null) {
                feedModel.setSearch_type(response.get(Constant.DocumentFieldName.SEARCH_TYPE).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SEARCH_TYPE).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.PUBLISHED_FIELD) != null) {
                feedModel.setPublished_at(response.get(Constant.DocumentFieldName.PUBLISHED_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.PUBLISHED_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.CHANNEL_FIELD) != null) {
                feedModel.setChannel(response.get(Constant.DocumentFieldName.CHANNEL_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CHANNEL_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.COLLECTOR_FIELD) != null) {
                feedModel.setCollector(response.get(Constant.DocumentFieldName.COLLECTOR_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.COLLECTOR_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.TITLE_FIELD) != null) {
                feedModel.setTitle(response.get(Constant.DocumentFieldName.TITLE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.TITLE_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.AUTHOR_FIELD) != null) {
                feedModel.setWriter(response.get(Constant.DocumentFieldName.AUTHOR_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.AUTHOR_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD) != null) {
                feedModel.setOg_image_base64(response.get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD).getValues().toString().length()-1));
            } else if (response.get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD) != null) {
                feedModel.setOg_image_url(response.get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD) != null) {
                feedModel.setFavicon_base64(response.get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD).getValues().toString().length()-1));
            } else if (response.get(Constant.DocumentFieldName.FAVICON_URL_FIELD) != null) {
                feedModel.setFavicon_url(response.get(Constant.DocumentFieldName.FAVICON_URL_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.FAVICON_URL_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.RECOMMEND_DATE_FIELD) != null) {
                feedModel.setRecommend_date(response.get(Constant.DocumentFieldName.RECOMMEND_DATE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.RECOMMEND_DATE_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.RECOMMENDATION_TYPE_FIELD) != null) {
                feedModel.setRecommendation_type(response.get(Constant.DocumentFieldName.RECOMMENDATION_TYPE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.RECOMMENDATION_TYPE_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.IS_RECOMMEND_FIELD) != null) {
                feedModel.setIsRecommend(response.get(Constant.DocumentFieldName.IS_RECOMMEND_FIELD).getValue().toString().equalsIgnoreCase("true"));
            }
            if (response.get(Constant.DocumentFieldName.IS_BOOKMARK_FIELD) != null) {
                feedModel.setIsBookmarked(response.get(Constant.DocumentFieldName.IS_BOOKMARK_FIELD).getValue().toString().equalsIgnoreCase("true"));
            }
            if (response.get(Constant.DocumentFieldName.IS_FEED_FIELD) != null) {
                feedModel.setIsFeed(response.get(Constant.DocumentFieldName.IS_FEED_FIELD).getValue().toString().equalsIgnoreCase("true"));
            }
            if (response.get(Constant.DocumentFieldName.DESCRIPTION_FIELD) != null) {
                feedModel.setDescription(response.get(Constant.DocumentFieldName.DESCRIPTION_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.DESCRIPTION_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.WRITER_SEARCH_FIELD) != null) {
                feedModel.setWriter_search(response.get(Constant.DocumentFieldName.WRITER_SEARCH_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.WRITER_SEARCH_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.FEED_PARTITION_FIELD) != null) {
                feedModel.setFeed_partition(response.get(Constant.DocumentFieldName.FEED_PARTITION_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.FEED_PARTITION_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.CONTENT_PARTITION_FIELD) != null) {
                feedModel.setContent_partition(response.get(Constant.DocumentFieldName.CONTENT_PARTITION_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CONTENT_PARTITION_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.TAGS_FIELD) != null) {
                feedModel.setTags(response.get(Constant.DocumentFieldName.TAGS_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.TAGS_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.WRITER_ID_FIELD) != null) {
                feedModel.setWriter_id(response.get(Constant.DocumentFieldName.WRITER_ID_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.WRITER_ID_FIELD).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.FEED_ID) != null) {
                feedModel.setFeed_id(response.get(Constant.DocumentFieldName.FEED_ID).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.FEED_ID).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.SOURCE_FIELD) != null) {
                feedModel.setUrl(response.get(Constant.DocumentFieldName.SOURCE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SOURCE_FIELD).getValues().toString().length()-1));
            }

        } catch (Exception e) {
            logger.error("can not convert to FeedDocument cause by: " + e.getMessage());
        }

        return feedModel;
    }

    public static String languageToLang(String substring) {
        Locale.setDefault(Locale.ENGLISH);
        for(Locale locale : Locale.getAvailableLocales()){
            if(substring.equalsIgnoreCase(locale.getDisplayLanguage(Locale.ENGLISH))){
                return locale.getLanguage();
            }
        }
        return substring;
    }

    public static List<String> getStoreFields(){
//        return new ArrayList<>(Arrays.asList("connectomeId", "feed_content_id","docId_content","liked","tags","type","isDeleted","created_date","created_by","service_language","keyword","search_type", "published_at", "channel", "collector", "title", "writer", "og_image_url", "og_image_base64", "favicon_url", "favicon_base64"));
        return new ArrayList<>(Arrays.asList(
                Constant.DocumentFieldName.CONNECTOME_ID_FIELD,
                Constant.DocumentFieldName.FEED_CONTENT_ID_FIELD,
                Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD,
                Constant.DocumentFieldName.LIKED_FIELD,
                Constant.DocumentFieldName.TYPE_FEED_FIELD,
                Constant.DocumentFieldName.IS_DELETED_FIELD,
                Constant.DocumentFieldName.CREATED_DATE_FIELD,
                Constant.DocumentFieldName.CREATED_BY_FIELD,
                Constant.DocumentFieldName.LANGUAGE_FIELD,
                Constant.DocumentFieldName.KEYWORD_FIELD,
                Constant.DocumentFieldName.SEARCH_TYPE,
                Constant.DocumentFieldName.PUBLISHED_FIELD,
                Constant.DocumentFieldName.CHANNEL_FIELD,
                Constant.DocumentFieldName.COLLECTOR_FIELD,
                Constant.DocumentFieldName.TITLE_FIELD,
                Constant.DocumentFieldName.WRITER_SEARCH_FIELD,
                Constant.DocumentFieldName.AUTHOR_FIELD,
                Constant.DocumentFieldName.OG_IMAGE_URL_FIELD,
                Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD,
                Constant.DocumentFieldName.FAVICON_URL_FIELD,
                Constant.DocumentFieldName.FAVICON_BASE64_FIELD,
//                Constant.DocumentFieldName.RECOMMEND_DATE_FIELD,
//                Constant.DocumentFieldName.RECOMMENDATION_TYPE_FIELD,
                Constant.DocumentFieldName.IS_RECOMMEND_FIELD,
                Constant.DocumentFieldName.IS_FEED_FIELD,
                Constant.DocumentFieldName.DESCRIPTION_FIELD,
                Constant.DocumentFieldName.IS_BOOKMARK_FIELD,
                Constant.DocumentFieldName.FEED_ID,
                Constant.DocumentFieldName.FEED_PARTITION_FIELD,
                Constant.DocumentFieldName.CONTENT_PARTITION_FIELD,
                Constant.DocumentFieldName.TAGS_FIELD,
                Constant.DocumentFieldName.SOURCE_FIELD,
                Constant.DocumentFieldName.WRITER_ID_FIELD
        ));
    }
}
