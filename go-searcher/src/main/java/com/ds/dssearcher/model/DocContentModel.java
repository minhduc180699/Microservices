package com.ds.dssearcher.model;

import com.ds.dssearcher.util.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;

import static com.ds.dssearcher.model.FeedModel.languageToLang;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocContentModel {

    private String docId;
    private String url;
    private String service_type;
    private String writer;
    private String search_type;
    private String origin_date;
    private String source_id;
    private String favicon_url;
    private String channel;
    private String description;
    private String title;
    private String content;
    private String collector;
    private String html_content;
    private List<Object> image_base64;
    private String og_image_base64;
    private String published_at;
    private String keyword;
    private String og_image_url;
    private String favicon_base64;
    private List<Object> image_url;

    private String category_url;
    private String web_source_category;
    private String subdomain_url;
    private String feed_id;
    private String video_url;
    private String domain_url;
    private String project_id;
    private String web_source_id;
    private String writer_id;
//    private String service_language;
    private String lang;
    private String video_base64;
    private String comment_id;
    private String subcategory_url;
    private String collected_at;
    private String tags;
    private String content_search;
    private String reply_id;
    private String category;
//    private String writer_search;
    private String content_partition;


    private static final Logger logger = LoggerFactory.getLogger(FeedModel.class);

    public static DocContentModel convertToFeedDocument(SearchHit hit) {
        if (hit.getId() == null) {
            return null;
        }

        DocContentModel docContentModel = new DocContentModel();
        Map<String, DocumentField> response = hit.getFields();

        try {

            if (hit.getId() != null) {
                docContentModel.setDocId(hit.getId());
            }
            if (response.get(Constant.DocumentFieldName.AUTHOR_FIELD) != null) {
                docContentModel.setWriter(response.get(Constant.DocumentFieldName.AUTHOR_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.AUTHOR_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.TITLE_FIELD) != null) {
                docContentModel.setTitle(response.get(Constant.DocumentFieldName.TITLE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.TITLE_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.SOURCE_FIELD) != null) {
                docContentModel.setUrl(response.get(Constant.DocumentFieldName.SOURCE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SOURCE_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.SOURCE_ID) != null) {
                docContentModel.setSource_id(response.get(Constant.DocumentFieldName.SOURCE_ID).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SOURCE_ID).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.KEYWORD_FIELD) != null) {
                docContentModel.setKeyword(response.get(Constant.DocumentFieldName.KEYWORD_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.KEYWORD_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.ORIGIN_DATE_FIELD) != null) {
                docContentModel.setOrigin_date(response.get(Constant.DocumentFieldName.ORIGIN_DATE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.ORIGIN_DATE_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.SEARCH_TYPE) != null) {
                docContentModel.setSearch_type(response.get(Constant.DocumentFieldName.SEARCH_TYPE).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SEARCH_TYPE).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD) != null) {
                docContentModel.setService_type(response.get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SERVICE_TYPE_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.FAVICON_URL_FIELD) != null) {
                docContentModel.setFavicon_url(response.get(Constant.DocumentFieldName.FAVICON_URL_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.FAVICON_URL_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD) != null) {
                docContentModel.setFavicon_base64(response.get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.FAVICON_BASE64_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD) != null) {
                docContentModel.setOg_image_url(response.get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD) != null) {
                docContentModel.setOg_image_base64(response.get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.IMAGE_URL_FIELD) != null) {
                docContentModel.setImage_url(response.get(Constant.DocumentFieldName.IMAGE_URL_FIELD).getValues());
            }
            if (response.get(Constant.DocumentFieldName.IMAGE_BASE64_FIELD) != null) {
                docContentModel.setImage_base64(response.get(Constant.DocumentFieldName.IMAGE_BASE64_FIELD).getValues());

            }
            if (response.get(Constant.DocumentFieldName.CONTENT_FIELD) != null) {
                docContentModel.setContent(response.get(Constant.DocumentFieldName.CONTENT_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CONTENT_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.PUBLISHED_FIELD) != null) {
                docContentModel.setPublished_at(response.get(Constant.DocumentFieldName.PUBLISHED_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.PUBLISHED_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.HTML_CONTENT_FIELD) != null) {
                docContentModel.setHtml_content(response.get(Constant.DocumentFieldName.HTML_CONTENT_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.HTML_CONTENT_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.DESCRIPTION_FIELD) != null) {
                docContentModel.setDescription(response.get(Constant.DocumentFieldName.DESCRIPTION_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.DESCRIPTION_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.CHANNEL_FIELD) != null) {
                docContentModel.setChannel(response.get(Constant.DocumentFieldName.CHANNEL_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CHANNEL_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.COLLECTOR_FIELD) != null) {
                docContentModel.setCollector(response.get(Constant.DocumentFieldName.COLLECTOR_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.COLLECTOR_FIELD).getValues().toString().length()-1));

            }

            if (response.get(Constant.DocumentFieldName.CATEGORY_FIELD) != null) {
                docContentModel.setCategory(response.get(Constant.DocumentFieldName.CATEGORY_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CATEGORY_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.WEB_SOURCE_CATEGORY) != null) {
                docContentModel.setWeb_source_category(response.get(Constant.DocumentFieldName.WEB_SOURCE_CATEGORY).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.WEB_SOURCE_CATEGORY).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD) != null) {
                docContentModel.setSubdomain_url(response.get(Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.FEED_ID) != null) {
                docContentModel.setFeed_id(response.get(Constant.DocumentFieldName.FEED_ID).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.FEED_ID).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.VIDEO_URL) != null) {
                docContentModel.setVideo_url(response.get(Constant.DocumentFieldName.VIDEO_URL).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.VIDEO_URL).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.DOMAIN_URL_FIELD) != null) {
                docContentModel.setDomain_url(response.get(Constant.DocumentFieldName.DOMAIN_URL_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.DOMAIN_URL_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.PROJECT_ID) != null) {
                docContentModel.setProject_id(response.get(Constant.DocumentFieldName.PROJECT_ID).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.PROJECT_ID).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.WEB_SOURCE_ID) != null) {
                docContentModel.setWeb_source_id(response.get(Constant.DocumentFieldName.WEB_SOURCE_ID).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.WEB_SOURCE_ID).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.WRITER_ID_FIELD) != null) {
                docContentModel.setWriter_id(response.get(Constant.DocumentFieldName.WRITER_ID_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.WRITER_ID_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.LANGUAGE_FIELD) != null) {
                docContentModel.setLang(languageToLang(response.get(Constant.DocumentFieldName.LANGUAGE_FIELD).getValue().toString()));

            }
            if (response.get(Constant.DocumentFieldName.VIDEO_BASE_64) != null) {
                docContentModel.setVideo_base64(response.get(Constant.DocumentFieldName.VIDEO_BASE_64).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.VIDEO_BASE_64).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.COMMENT_ID) != null) {
                docContentModel.setComment_id(response.get(Constant.DocumentFieldName.COMMENT_ID).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.COMMENT_ID).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.SUB_CATEGORY_FIELD) != null) {
                docContentModel.setSubcategory_url(response.get(Constant.DocumentFieldName.SUB_CATEGORY_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SUB_CATEGORY_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.COLLECTED_AT_FIELD) != null) {
                docContentModel.setCollected_at(response.get(Constant.DocumentFieldName.COLLECTED_AT_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.COLLECTED_AT_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.TAGS_FIELD) != null) {
                docContentModel.setTags(response.get(Constant.DocumentFieldName.TAGS_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.TAGS_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.SEARCH_CONTENT_FIELD) != null) {
                docContentModel.setContent_search(response.get(Constant.DocumentFieldName.SEARCH_CONTENT_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.SEARCH_CONTENT_FIELD).getValues().toString().length()-1));

            }
            if (response.get(Constant.DocumentFieldName.REPLY_ID) != null) {
                docContentModel.setReply_id(response.get(Constant.DocumentFieldName.REPLY_ID).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.REPLY_ID).getValues().toString().length()-1));
            }
            if (response.get(Constant.DocumentFieldName.CATEGORY) != null) {
                docContentModel.setCategory(response.get(Constant.DocumentFieldName.CATEGORY).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CATEGORY).getValues().toString().length()-1));
            }
//            if (response.get(Constant.DocumentFieldName.WRITER_SEARCH_FIELD) != null) {
//                feedContentModel.setWriter_search(response.get(Constant.DocumentFieldName.WRITER_SEARCH_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.WRITER_SEARCH_FIELD).getValues().toString().length()-1));
//            }
            if (response.get(Constant.DocumentFieldName.CONTENT_PARTITION_FIELD) != null) {
                docContentModel.setContent_partition(response.get(Constant.DocumentFieldName.CONTENT_PARTITION_FIELD).getValues().toString().substring(1,response.get(Constant.DocumentFieldName.CONTENT_PARTITION_FIELD).getValues().toString().length()-1));
            }
        } catch (Exception e) {
            logger.error("can not convert to FeedDocument cause by: " + e.getMessage());
        }

        return docContentModel;
    }

    public static String[] getListIndexByDocIds(List<String> contentIds) {
        Set<String> indexes = new HashSet<>();
        for (String contentId : contentIds) {
            indexes.add(contentId.substring(0, contentId.indexOf("-", 12) + 1) + "*");
        }
        String[] result = new String[indexes.size()];
        return indexes.toArray(result);
    }
    public static String getIndexByDocId(String contentId) {
        return contentId.substring(0, contentId.indexOf("-", 12) + 1) + "*";
    }

    public static List<String> getStoreFields() {
        return new ArrayList<>(Arrays.asList(
                Constant.DocumentFieldName.AUTHOR_FIELD,
//                Constant.DocumentFieldName.WRITER_SEARCH_FIELD,
                Constant.DocumentFieldName.TITLE_FIELD,
//                Constant.DocumentFieldName.URL_FIELD,
                Constant.DocumentFieldName.SOURCE_ID,
                Constant.DocumentFieldName.KEYWORD_FIELD,
                Constant.DocumentFieldName.ORIGIN_DATE_FIELD,
                Constant.DocumentFieldName.SEARCH_TYPE,
                Constant.DocumentFieldName.SERVICE_TYPE_FIELD,
//                Constant.DocumentFieldName.FAVICON_URL_FIELD,
                Constant.DocumentFieldName.FAVICON_BASE64_FIELD,
                Constant.DocumentFieldName.OG_IMAGE_URL_FIELD,
                Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD,
                Constant.DocumentFieldName.IMAGE_URL_FIELD,
                Constant.DocumentFieldName.IMAGE_BASE64_FIELD,
                Constant.DocumentFieldName.CONTENT_FIELD,
                Constant.DocumentFieldName.PUBLISHED_FIELD,
//                Constant.DocumentFieldName.CHANNEL_FIELD,
                Constant.DocumentFieldName.COLLECTOR_FIELD,
                Constant.DocumentFieldName.CONTENT_PARTITION_FIELD,
                Constant.DocumentFieldName.SOURCE_FIELD,
                Constant.DocumentFieldName.FEED_ID,

                Constant.DocumentFieldName.DESCRIPTION_FIELD,
                Constant.DocumentFieldName.HTML_CONTENT_FIELD,
//                Constant.DocumentFieldName.WEB_SOURCE_CATEGORY,
                Constant.DocumentFieldName.VIDEO_URL,
                Constant.DocumentFieldName.WRITER_ID_FIELD,
                Constant.DocumentFieldName.LANGUAGE_FIELD,
                Constant.DocumentFieldName.VIDEO_BASE_64,
                Constant.DocumentFieldName.COLLECTED_AT_FIELD,
                Constant.DocumentFieldName.TAGS_FIELD,
//                Constant.DocumentFieldName.SEARCH_CONTENT_FIELD,
                Constant.DocumentFieldName.CATEGORY
        ));
    }
}
