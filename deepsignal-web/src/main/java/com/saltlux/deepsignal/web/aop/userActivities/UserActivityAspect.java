package com.saltlux.deepsignal.web.aop.userActivities;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Comment;
import com.saltlux.deepsignal.web.domain.UserActivityLog;
import com.saltlux.deepsignal.web.service.dto.FilterFeedDTO;
import com.saltlux.deepsignal.web.util.HttpRequestResponseUtils;
import com.saltlux.deepsignal.web.util.ICache;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserActivityAspect {

    // keyword, sort and filter to check search, filter or sort activity in feed
    private String keyword = null;
    private String sort = "desc";
    private List<FilterFeedDTO> listFilter = new ArrayList<>();

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    private List<FilterFeedDTO> getListFilter() {
        return listFilter;
    }

    private void setListFilter(List<FilterFeedDTO> listFilter) {
        this.listFilter = listFilter;
    }

    private ICache iCache;

    private ApplicationContext applicationContext;

    private ApplicationProperties applicationProperties;

    public UserActivityAspect(
        @Qualifier("redis") ICache iCache,
        ApplicationContext applicationContext,
        ApplicationProperties applicationProperties
    ) {
        this.iCache = iCache;
        this.applicationContext = applicationContext;
        this.applicationProperties = applicationProperties;
    }

    @Before("@annotation(userActivity)")
    public void saveUserActivity(JoinPoint joinPoint, UserActivity userActivity) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] paramNames = methodSignature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        Map<String, Object> mapParams = IntStream
            .range(0, paramNames.length)
            .collect(HashMap::new, (m, i) -> m.put(String.valueOf(paramNames[i]), paramValues[i]), Map::putAll);

        // skip scroll in feed
        if (
            userActivity.activityName().equals(Constants.UserActivities.UNKNOWN_SEARCH_FILTER_SORT) &&
            mapParams.get("page") != null &&
            (Integer) mapParams.get("page") > 0
        ) {
            return;
        }

        UserActivityLog userActivityLog = new UserActivityLog();
        String activityName = null;
        if (HttpRequestResponseUtils.getCurrentUser() != null) {
            userActivityLog.setUsername(HttpRequestResponseUtils.getCurrentUser().getUsername());
            userActivityLog.setUserInfo(HttpRequestResponseUtils.getLoggedInUser());
            userActivityLog.setUserIp(HttpRequestResponseUtils.getClientIpAddress());
            userActivityLog.setRequestMethod(HttpRequestResponseUtils.getRequestMethod());
            userActivityLog.setURL(HttpRequestResponseUtils.getRequestUrl());
            userActivityLog.setPage(HttpRequestResponseUtils.getRequestUri());
            userActivityLog.setQueryParams(HttpRequestResponseUtils.getPageQueryString());
            userActivityLog.setRefererPage(HttpRequestResponseUtils.getRefererPage());
            userActivityLog.setUserAgent(HttpRequestResponseUtils.getUserAgent());
            userActivityLog.setFeedTitle(HttpRequestResponseUtils.getHeader(Constants.Header.TITLE));
            userActivityLog.setFeedOriginalUrl(HttpRequestResponseUtils.getHeader(Constants.Header.ORIGINAL_URL));
        }
        switch (userActivity.activityName()) {
            case Constants.UserActivities.UNKNOWN_SEARCH_FILTER_SORT:
                // clear keyword
                if (getKeyword() != null) {
                    if (mapParams.get("keyword") == null || mapParams.get("keyword") == "") {
                        setKeyword(null);
                    }
                }

                if (mapParams.get("keyword") != null && mapParams.get("keyword") != "") {
                    userActivityLog.setRelatedKeywordSearch(String.valueOf(mapParams.get("keyword")));
                    if (!mapParams.get("keyword").equals(getKeyword())) {
                        setKeyword(String.valueOf(mapParams.get("keyword")));
                        activityName = Constants.UserActivities.SEARCH_FEED;
                    }
                }
                if (!String.valueOf(mapParams.get("sortDirection")).equals(getSort())) {
                    setSort(String.valueOf(mapParams.get("sortDirection")));
                    activityName = Constants.UserActivities.SORT;
                }
                List<FilterFeedDTO> listFilterParam = (List<FilterFeedDTO>) mapParams.get("filterFeedDTOS");
                if (listFilterParam.size() > 0) {
                    listFilterParam.sort(Comparator.comparing(FilterFeedDTO::getField));
                    if (!listFilterParam.toString().equals(getListFilter().toString())) {
                        setListFilter(listFilterParam);
                        activityName = Constants.UserActivities.FILTER;
                    }
                } else {
                    this.listFilter.clear();
                }
                break;
            case Constants.UserActivities.SEARCH_LEARNING:
                activityName = Constants.UserActivities.SEARCH_LEARNING;
                if (mapParams.get("keyword") != null && !mapParams.get("keyword").equals("")) {
                    userActivityLog.setRelatedKeywordSearch(String.valueOf(mapParams.get("keyword")));
                }
                break;
            case Constants.UserActivities.COMMENT:
                activityName = Constants.UserActivities.COMMENT;
                Comment comment = (Comment) mapParams.get("comment");
                mapParams.put("feedId", comment.getFeedId());
                break;
            case Constants.UserActivities.UNKNOWN_ACTIVITY:
                if (mapParams.get("activity") != null && !mapParams.get("activity").equals("")) {
                    String docId = mapParams.get("docId").toString();
                    userActivityLog.setFeedId(docId);
                    if (mapParams.get("activity").equals(Constants.UserActivities.BOOKMARK)) {
                        boolean stateBookmark = (boolean) mapParams.get("state");
                        if (stateBookmark) {
                            activityName = Constants.UserActivities.BOOKMARK;
                        } else {
                            activityName = Constants.UserActivities.UN_BOOKMARK;
                        }
                    } else if (mapParams.get("activity").equals(Constants.UserActivities.LIKE)) {
                        Integer likeValue = (Integer) mapParams.get("likeState");
                        switch (likeValue) {
                            case 1:
                                activityName = Constants.UserActivities.LIKE;
                                break;
                            case 2:
                                activityName = Constants.UserActivities.DISLIKE;
                                break;
                            default:
                                activityName = Constants.UserActivities.UNLIKE;
                                break;
                        }
                    } else if (mapParams.get("activity").equals(Constants.UserActivities.HIDDEN)) {
                        activityName = Constants.UserActivities.DELETE;
                    }
                }
                break;
            default:
                activityName = userActivity.activityName();
                break;
        }

        if (activityName != null) {
            userActivityLog.setActivityName(activityName);
            if (activityName.equals(Constants.UserActivities.HIDDEN)) {
                String feedback = (String) mapParams.get("feedback");
                if (StringUtils.isNotEmpty(feedback)) {
                    userActivityLog.setUserFeedback(feedback);
                }
            }
            if (mapParams.get("connectomeId") != null && mapParams.get("connectomeId") != "") {
                userActivityLog.setConnectomeId(String.valueOf(mapParams.get("connectomeId")));
            }
            if (mapParams.get("feedId") != null && mapParams.get("feedId") != "") {
                userActivityLog.setFeedId(String.valueOf(mapParams.get("feedId")));
            }
            // push to cache
            this.iCache.pushActivityToCache(userActivityLog);
        }
    }

    // start user activity consumer
    @PostConstruct
    public void startConsumer() {
        ExecutorService executor = Executors.newFixedThreadPool(applicationProperties.getUserActivity().getNumberThreadConsumer());
        executor.execute(applicationContext.getBean(UserActivityConsumer.class));
    }
}
