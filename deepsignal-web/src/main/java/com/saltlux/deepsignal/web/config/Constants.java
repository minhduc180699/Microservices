package com.saltlux.deepsignal.web.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String PHONE_NUMBER_TEST = "5571981265131";

    // External API URI
    public static final String ADD_CONDITION_URI = "/websource/add-condition";
    public static final String SAVE_ALL_WEBSOURCE_URI = "/websources";
    public static final String KAFKA_PUBLISH_FILE_INFO = "/kafka/publish";
    public static final String BIGO_SEARCH_URI = "/bigo/search";
    public static final String TIME_SERIES = "/deepsignal/trends/getTimeseries";
    public static final String META_SEARCH = "/deepsignal/realtime/metasearch/getMetaSearch";
    public static final String META_SEARCH_CACHE = "/metasearch-cache/";
    public static final String FIND_PEOPLE = "/peoples/find";
    public static final String GET_IMAGE = "/image/get";
    public static final String CONNECTOME_TRAINING = "/connectomes/training";
    public static final String GET_CONNECTOMES_URI = "/connectomes";
    public static final String POST_MINI_CONNECTOMES_URI = "/connectomes/training/instant/ids";
    public static final String POST_TEXT_CONNECTOME_URI = "/connectomes/training/instant/text/pageRank";
    public static final String POST_CONTEXTUAL_MEMORY_GET_COLLECTION_ID_URI = "/contextualMemory/getCollectionId";
    public static final String POST_CONTEXTUAL_MEMORY_CREATE_COLLECTION_URI = "/contextualMemory/createCollection";
    public static final String GET_CONTEXTUAL_MEMORY_GET_LIST_COLLECTION_URI = "/contextualMemory/ctxmmList";
    public static final String GET_CONTEXTUAL_MEMORY_GET_COLLECTION_LIST_URI = "/contextualMemory/getCollectionList";
    public static final String POST_CONTEXTUAL_MEMORY_UPDATE_URI = "/contextualMemory/updateCollection";
    public static final String PUT_CONTEXTUAL_MEMORY_UPSERT_URI = "/contextualMemory/upsert";
    public static final String DELETE_CONTEXTUAL_MEMORY_DELETE_URI = "/contextualMemory/delete";
    public static final String GET_CONTEXTUAL_MEMORY_GET_COLLECTION_URI = "/contextualMemory/getCollection";
    public static final String GET_RECOMMENDATION_GET_REQUESTLIST_URI = "/rc/v2/start/feed/recommendation_tmp";
    public static final String PATCH_DISABLE_ENTITY_URI = "/vertex/toggle/disable";
    public static final String PATCH_FAVORITE_ENTITY_URI = "/vertex/toggle/favorite";
    public static final String GET_CONNECTOMES_STATUS_URI = "/status";
    public static final String POST_CONNECTOMES_ENTITIES_DETAIL_URI = "/entity/multipleGet";
    public static final String GET_ENTITIES_NETWORK_DETAIL_URI = "/entitynetwork";
    public static final String GET_IMAGES = "/images/get";
    public static final String DOC_CONVERT = "/deepsignalconverter/documentconverter/urlconvert";
    public static final String URL_PREVIEW = "/deepsignalconverter/documentparser/articlecontent";
    public static final String RECOMMEND_JOB_FEED = "/recommend";

    public static final Integer SEND_CODE_LOGIN_ACTION = 1;
    public static final Integer NUMBER_OF_ACTIVITY_TO_SAVE = 2;

    public enum UploadType {
        FILE,
        URL,
        DOC,
        DOWNLOAD,
        WEB,
        ALL,
    }

    public enum MetaSearchType {
        news,
        web,
        video,
    }

    public enum SignalIssues {
        IT,
        TI,
    }

    public enum PostType {
        PERSON(410),
        ORGANIZATION(411),
        LOCATION(412),
        BLOG(413),
        ARTICLE(510),
        FILE(511),
        MOVIE(515),
        MUSIC(516),
        TABLE(520),
        TERM_LIST(521),
        WORD_CLOUD(522),
        ENTITY(540),
        TOP_TEN(560),
        LINE_GRAPH(610),
        VERTICAL_BAR_GRAPH(611),
        HORIZONTAL_BAR_GRAPH(612),
        PIPE_CHART(613),
        STACK(700),
        WEATHER(720),
        MAP(740);

        public final Integer postType;

        PostType(Integer postType) {
            this.postType = postType;
        }
    }

    public enum TypeInteraction {
        LIKE(1),
        DISLIKE(2),
        SHARE(3),
        BOOKMARK(4),
        COMMENT(5),
        DELETE(6);

        public final Integer type;

        TypeInteraction(Integer type) {
            this.type = type;
        }
    }

    public enum NotificationCategory {
        basic,
        interpolation,
    }

    public enum NotificationName {
        LEARNING_COMPLETE("learning completed"),
        COMMENT_FEED("comment feed"),
        REPLY_COMMENT("reply comment"),
        TRAINING_COMPLETED("training completed");

        public final String name;

        NotificationName(String name) {
            this.name = name;
        }
    }

    public enum PlatformSharing {
        FACEBOOK("FACEBOOK"),
        TWITTER("TWITTER"),
        LINKEDIN("LINKEDIN"),
        LINK("LINK");

        public final String type;

        PlatformSharing(String type) {
            this.type = type;
        }
    }

    public enum StateOfShareLink {
        PUBLIC("PUBLIC"),
        RESTRICTED("RESTRICTED");

        public final String type;

        StateOfShareLink(String type) {
            this.type = type;
        }
    }

    public static class Type {

        public static final Integer TYPE_FILE_UPLOAD = 1;
        public static final Integer TYPE_FILE_INQUIRY = 2;
        public static final Integer TYPE_FILE_INQUIRY_QUESTION = 1;
        public static final Integer TYPE_FILE_INQUIRY_ANSWER = 2;
    }

    /**
     * Define Error Keys
     */
    public enum ErrorKeys {
        IDEXISTS("idexists"),
        IDUSERNULL("idusernull"),
        LABELENTITYNULL("labelentitynull");

        public final String label;

        ErrorKeys(String label) {
            this.label = label;
        }
    }

    /**
     * Define Error Codes
     */
    public enum ErrorCode {
        DEEPSINAL_VERIFY_COUNTRY_CODE_BLANK("001", "deepsignal.verify.country.code.blank"),
        DEEPSINAL_VERIFY_PHONE_BLANK("002", "deepsignal.verify.phone.blank"),
        DEEPSINAL_VERIFY_PHONE_INCORRECT_FORMAT("003", "deepsignal.verify.phone.format"),
        DEEPSINAL_VERIFY_PHONE_INCORRECT("004", "deepsignal.verify.phone.incorrect"),
        DEEPSINAL_VERIFY_CODE_BLANK("005", "deepsignal.verify.code.blank"),
        DEEPSINAL_VERIFY_CODE_EXPIRED("006", "deepsignal.verify.code.expired"),
        DEEPSINAL_VERIFY_CODE_INCORRECT("007", "deepsignal.verify.code.incorrect"),
        DEEPSINAL_VERIFY_SEND_CODE_ERROR("008", "deepsignal.verify.send.code.error"),

        //error register

        DEEPSINAL_REGISTER_ACCOUNT_UNCONFIRM("009", "deepsignal.register.account.unconfirmed"),
        DEEPSINAL_REGISTER_ACCOUNT_EXISTED("010", "deepsignal.register.account.existed"),
        DEEPSINAL_REGISTER_ACCOUNT_NOT_EXISTED("011", "deepsignal.register.account.notexisted"),
        DEEPSINAL_REGISTER_ACCOUNT_TEMPORARY("012", "deepsignal.register.account.temporary"),
        DEEPSINAL_REGISTER_CONNECTOME_EMPTY("013", "deepsinal.register.connectome.empty"),
        DEEPSINAL_REGISTER_PURPOSE_NULL("014", "deepsinal.register.purpose.null"),
        DEEPSINAL_FILE_EXISTS("015", "deepsinal.file.exists"),
        DEEPSINAL_FILE_UPLOAD_ERROR("016", "deepsinal.file.upload.error"),
        DEEPSINAL_FILE_INCORRECT_FORMAT("017", "deepsinal.file.incorrect.format"),
        DEEPSINAL_FILE_MAX_SIZE("018", "deepsinal.file.maxsize"),
        DEEPSINAL_USER_ID_NULL("019", "deepsinal.user.id.null"),
        DEEPSINAL_FAQ_ID_NOT_NULL("020", "deepsinal.faq.id.notnull"),
        DEEPSINAL_CATEGORY_ID_EMPTY("021", "deepsinal.category.id.null"),
        DEEPSINAL_INQUIRY_QUESTION_ID_NOT_NULL("022", "deepsinal.inquiry.question.id.notnull"),
        DEEPSINAL_INQUIRY_QUESTION_ID_NULL("023", "deepsinal.inquiry.question.id.notnull"),
        DEEPSINAL_INQUIRY_ANSWER_ID_NOT_NULL("024", "deepsinal.inquiry.answer.id.notnull"),

        DEEPSIGNAL_CHECK_EMAIL_EXISTED("025", "deepsignal.check.email.existed"),
        DEEPSIGNAL_SEND_EMAIL_FALSE("026", "deepsignal.send.email.error"),
        DEEPSIGNAL_VERIFY_EMAIL_CODE_BLANK("027", "deepsignal.verify.email.code.blank"),
        DEEPSIGNAL_VERIFY_EMAIL_CODE_INCORRECT("028", "deepsignal.verify.email.code.incorrect"),

        DEEPSIGNAL_UPLOAD_CONNECTOME_IMAGE_PROFILE_FALSE("029", "deepsignal.upload.connectome.image.profile.false"),
        DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED("030", "deepsignal.user.id.not.existed"),

        DEEPSIGNAL_ERROR("044", "FAILURE"),
        DEEPSIGNAL_SUCCESS("000", "SUCCESS");

        public final String code;
        public final String description;

        ErrorCode(String code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public String toString() {
            return code + ": " + description;
        }
    }

    public static class UserActivities {

        public static final String LIKE = "like";
        public static final String DISLIKE = "dislike";
        public static final String UNLIKE = "unlike";
        public static final String SHARE = "share";
        public static final String BOOKMARK = "bookmark";
        public static final String UN_BOOKMARK = "bookmark";
        public static final String SEARCH_FEED = "search feed";
        public static final String SEARCH_LEARNING = "search learning center";
        public static final String PREVIEW_URL = "preview url learning center";
        public static final String CHANGE_URL = "change url";
        public static final String FILTER = "filter";
        public static final String SORT = "sort";
        public static final String FAVORITES = "favorites";
        public static final String ARCHIVES = "archives";
        public static final String COMMENT = "comment";
        public static final String READ_ARTICLE = "read article";
        public static final String MEMO = "memo";
        public static final String HIDDEN = "delete";
        public static final String SELECT_TOPIC_ENTITY = "select topic/entity";
        public static final String UPDATE_CONNECTOME_NAME = "update connectome name";
        public static final String UPDATE_USER_INFO = "update user information";
        public static final String VISIT_PEOPLE = "visit people";
        public static final String TRAINING = "training";
        public static final String DELETE = "delete document";

        // multiple activity in one api
        public static final String UNKNOWN_SEARCH_FILTER_SORT = "unknown-search-filter-sort";
        public static final String UNKNOWN_LIKE_UNLIKE_DISLIKE = "unknown-like-unlike-dislike";
        public static final String UNKNOWN_ACTIVITY = "unknown-bookmark-delete";
    }

    public static class Header {

        public static final String TITLE = "Title";
        public static final String ORIGINAL_URL = "Original-Url";
    }

    private Constants() {}

    public static class SearchFileType {

        public static final String PDF = "pdf";
        public static final String PPT = "ppt";
        public static final String DOC = "doc";
        public static final String PPTX = "pptx";
        public static final String DOCX = "docx";
        public static final String XLS = "xls";
        public static final String XLSX = "xlsx";
        public static final String[] FILETYPE = { "pdf", "ppt", "pptx", "doc", "docx", "xls", "xlsx" };
    }
}
