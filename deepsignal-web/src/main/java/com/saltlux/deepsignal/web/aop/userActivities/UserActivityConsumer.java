package com.saltlux.deepsignal.web.aop.userActivities;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.UserActivityLog;
import com.saltlux.deepsignal.web.service.impl.UserActivityLogService;
import com.saltlux.deepsignal.web.util.ICache;
import com.saltlux.deepsignal.web.util.RedisCache;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UserActivityConsumer implements Runnable {

    private ICache iCache;

    private UserActivityLogService userActivityLogService;

    private List<UserActivityLog> listUserActivityLog = new ArrayList<>();

    public UserActivityConsumer(@Qualifier("redis") ICache iCache, UserActivityLogService userActivityLogService) {
        this.iCache = iCache;
        this.userActivityLogService = userActivityLogService;
    }

    @Override
    public void run() {
        saveUserActivity();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void saveUserActivity() {
        UserActivityLog userActivityLog;
        while (true) {
            // take from cache
            userActivityLog = this.iCache.takeActivityFromCache();
            if (userActivityLog != null) {
                listUserActivityLog.add(userActivityLog);
            }
            if (listUserActivityLog.size() == Constants.NUMBER_OF_ACTIVITY_TO_SAVE) {
                userActivityLogService.saveAllUserActivityLogs(listUserActivityLog);
                listUserActivityLog.clear();
            }
        }
    }
}
