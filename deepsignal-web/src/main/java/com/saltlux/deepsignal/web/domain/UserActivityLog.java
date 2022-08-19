package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saltlux.deepsignal.web.util.HttpRequestResponseUtils;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

/**
 * A user activity log.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_activity_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserActivityLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11)", updatable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "username")
    private String username;

    @JoinColumn(name = "user_info")
    private String userInfo;

    private String feedId;

    @JoinColumn(name = "connectome_id")
    private String connectomeId;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "user_ip")
    private String userIp;

    @Column(name = "URL")
    private String URL;

    @Column(name = "referer_page")
    private String refererPage;

    @Column(name = "page")
    private String page;

    @Column(name = "query_params")
    private String queryParams;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "related_keyword_search")
    private String relatedKeywordSearch;

    @CreatedDate
    @Column(updatable = false, name = "logged_time")
    private Instant loggedTime = Instant.now();

    @Column(name = "feed_title")
    private String feedTitle;

    @Column(name = "feed_original_url")
    private String feedOriginalUrl;

    @Column(name = "user_feedback")
    private String userFeedback;

    @Column(name = "user_language")
    private String userLanguage;

    @Column(name = "training_data")
    private String trainingData;

    public UserActivityLog(UserActivityLog userActivityLog) {
        if (null != HttpRequestResponseUtils.getCurrentUser()) {
            this.username = HttpRequestResponseUtils.getCurrentUser().getUsername();
            this.userInfo = HttpRequestResponseUtils.getLoggedInUser();
            this.userIp = HttpRequestResponseUtils.getClientIpAddress();
            this.requestMethod = HttpRequestResponseUtils.getRequestMethod();
            this.URL = HttpRequestResponseUtils.getRequestUrl();
            this.page = HttpRequestResponseUtils.getRequestUri();
            this.queryParams = HttpRequestResponseUtils.getPageQueryString();
            this.refererPage = HttpRequestResponseUtils.getRefererPage();
            this.userAgent = HttpRequestResponseUtils.getUserAgent();
            this.loggedTime = Instant.now();
        }
        this.feedId = userActivityLog.getFeedId();
        this.connectomeId = userActivityLog.getConnectomeId();
        this.activityName = userActivityLog.getActivityName();
        this.relatedKeywordSearch = userActivityLog.getRelatedKeywordSearch();
        this.feedTitle = userActivityLog.getFeedTitle();
        this.feedOriginalUrl = userActivityLog.getFeedOriginalUrl();
        this.userFeedback = userActivityLog.getUserFeedback();
        this.userLanguage = userActivityLog.getUserLanguage();
        this.trainingData = userActivityLog.getTrainingData();
    }
}
