package com.saltlux.deepsignal.adapter.config;

import com.saltlux.deepsignal.adapter.service.dto.FilterFeedDTO;

/**
 * Application constants.
 */
public final class Constants {

    public static final String SYSTEM = "system";

    public static final String WEB_SOURCE_ENTITY = "web_source";

    public static final String BIGO_SEARCH = "/search";

    public static final String BIGO_SEARCH_NEXT = "/search/next";

    public static final String BIGO_SEARCH_TIMESERIES = "/search/timeseries";

    public static final String BIGO_REPOSITORY = "/find/repository";

    public static final String[] BIGO_INDEXES = { "ds-news", "ds-blog", "ds-twitter" };

    public static final FilterFeedDTO[] HIDE_CONDITIONS_FILTER = { new FilterFeedDTO("liked", 2), new FilterFeedDTO("deleted", true) };

    private Constants() {}

    public enum activity {
        MEMO("memo"),
        BOOKMARK("bookmark"),
        DELETE("delete"),
        LIKE("like"),
        DISLIKE("dislike");

        public final String type;

        activity(String type) {
            this.type = type;
        }
    }

    public static class PERSONAL_DOCUMENT_STATUS {

        public static final int DELETED = 1;
        public static final int NOT_DELETED = 0;
        public static final int LIKED = 1;
        public static final int DISLIKED = 2;
        public static final int MEMO = 1;
        public static final int BOOKMARKED = 1;
    }

    public enum page {
        FEED("feed"),
        PEOPLE("people"),
        LEARNING_CENTER("learning-center"),
        PERSONAL_DOCUMENT("personal-document");

        public final String type;

        page(String type) {
            this.type = type;
        }
    }

    public static class FEED_FILTER {

        public static final String RECOMMEND_DATE = "recommendDate";
        public static final String SEARCH_TYPE = "searchType";
    }
}
