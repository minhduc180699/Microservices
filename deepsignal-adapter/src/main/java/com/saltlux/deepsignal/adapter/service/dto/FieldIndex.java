package com.saltlux.deepsignal.adapter.service.dto;

public class FieldIndex {

    public static final String TRUE = "TRUE";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String service_type = "service_type";
    public static final String service_filter = "service_filter";
    public static final String service_language = "service_language";
    public static final String service_section = "service_section";

    public static final String project_id = "project_id";
    public static final String web_source_id = "web_source_id";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String published_at = "published_at";
    public static final String collected_at = "collected_at";

    public static final String _search = "_search"; //통합 검색 필드
    public static final String _search_bigram = "_search_bigram";
    public static final String _search_morph = "_search_morph";

    public static final String title = "title";
    public static final String title_bigram = "title_bigram";
    public static final String title_morph = "title_morph";

    public static final String content = "content";

    public static final String writer = "writer";

    public static final String images = "images";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String tms_prefix = "tms";

    public static final String tms_feature = "tms_feature";
    public static final String tms_similarity = "tms_similarity";

    public static final String tms_raw_stream = "tms_raw_stream";
    public static final String tms_raw_stream_index = "tms_raw_stream_index";
    public static final String tms_raw_stream_store = "tms_raw_stream_store";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //감성 분석 필드
    public static final String tms_sentiment_polarity = "tms_sentiment_polarity";
    public static final String tms_sentiment_polarity_score = "tms_sentiment_polarity_score";

    public static final String tms_sentiment_class = "tms_sentiment_class";
    public static final String tms_sentiment_positive_word = "tms_sentiment_positive_word";
    public static final String tms_sentiment_negative_word = "tms_sentiment_negative_word";

    public static final String tms_sentiment_positive_text = "tms_sentiment_positive_text";
    public static final String tms_sentiment_positive_text_bigram = "tms_sentiment_positive_text_bigram";

    public static final String tms_sentiment_negative_text = "tms_sentiment_negative_text";
    public static final String tms_sentiment_negative_text_bigram = "tms_sentiment_negative_text_bigram";

    public static final String POLARITY_POSITIVE = "POSITIVE"; //긍정 값
    public static final String POLARITY_NEGATIVE = "NEGATIVE"; //부정 값
    public static final String POLARITY_NEUTRAL = "NEUTRAL"; //중립 값

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //개체명 분석 필드
    public static final String tms_ne_prefix = "tms_ne_";
    public static final String tms_ne_person = "tms_ne_person";
    public static final String tms_ne_organization = "tms_ne_organization";
    public static final String tms_ne_location = "tms_ne_location";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String source_id = "source_id";
    public static final String comment_id = "comment_id";
    public static final String reply_id = "reply_id";

    public static final String source_uri = "source_uri";
    public static final String feed_id = "feed_id";

    public static final String message = "message";

    public static final String message_level = "message_level"; //원글,댓글, 답글을 표현하기 위해 사용

    public static class article {

        public static final String site = "site";
    }

    public static class tweet {

        public static final String tweet_id = "tweet_id";
        public static final String message = "message";

        public static final String language = "language";
        public static final String tags = "tags";
        public static final String geo_point = "geo_point";
        public static final String geo_shape = "geo_shape";
    }

    public static class facebook {

        public static final String comment_count = "comment_count";
        //final public static String language					="language";
        //final public static String tags						="tags";
        //final public static String geo_point					="geo_point";
        //final public static String geo_shape					="geo_shape";
    }
}
