package com.saltlux.deepsignal.web.security;

import com.saltlux.deepsignal.web.domain.UserActivityLog;
import com.saltlux.deepsignal.web.service.impl.UserActivityLogService;
import com.saltlux.deepsignal.web.util.HttpRequestResponseUtils;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserActivityLogger implements HandlerInterceptor {

    private UserActivityLogService userActivityLogService;

    public UserActivityLogger(UserActivityLogService userActivityLogService) {
        this.userActivityLogService = userActivityLogService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpRequestResponseUtils.getCurrentUser() != null) {
            //            final String ip = HttpRequestResponseUtils.getClientIpAddress();
            //            final String url = HttpRequestResponseUtils.getRequestUrl();
            //            final String page = HttpRequestResponseUtils.getRequestUri();
            //            final String refererPage = HttpRequestResponseUtils.getRefererPage();
            //            final String queryString = HttpRequestResponseUtils.getPageQueryString();
            //            final String userAgent = HttpRequestResponseUtils.getUserAgent();
            //            final String requestMethod = HttpRequestResponseUtils.getRequestMethod();
            //            final Instant timestamp = Instant.now();
            //
            //            UserActivityLog userActivityLog = new UserActivityLog();
            //            userActivityLog.setUsername(HttpRequestResponseUtils.getCurrentUser().getUsername());
            //            userActivityLog.setUserInfo(HttpRequestResponseUtils.getLoggedInUser());
            //            userActivityLog.setUserIp(ip);
            //            userActivityLog.setRequestMethod(requestMethod);
            //            userActivityLog.setURL(url);
            //            userActivityLog.setPage(page);
            //            userActivityLog.setQueryParams(queryString);
            //            userActivityLog.setRefererPage(refererPage);
            //            userActivityLog.setUserAgent(userAgent);
            //            userActivityLog.setLoggedTime(timestamp);

            userActivityLogService.saveUserActivityLogWithBase();

            return true;
        }

        return true;
    }
}
