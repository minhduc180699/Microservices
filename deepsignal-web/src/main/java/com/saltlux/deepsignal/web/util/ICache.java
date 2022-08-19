package com.saltlux.deepsignal.web.util;

import com.saltlux.deepsignal.web.domain.UserActivityLog;

public interface ICache {
    void pushActivityToCache(UserActivityLog userActivityLog);
    UserActivityLog takeActivityFromCache();
}
