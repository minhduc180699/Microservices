package com.saltlux.deepsignal.web.util;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.domain.UserActivityLog;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBlockingQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("hazelcast")
public class HazelcastCache implements ICache {

    private BlockingQueue<UserActivityLog> queueUserActivity;
    private ApplicationProperties applicationProperties;

    public HazelcastCache(BlockingQueue<UserActivityLog> queueUserActivity, ApplicationProperties applicationProperties) {
        this.queueUserActivity = queueUserActivity;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void pushActivityToCache(UserActivityLog userActivityLog) {
        try {
            queueUserActivity.offer(userActivityLog, applicationProperties.getUserActivity().getFullQueueWaitingTime(), TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public UserActivityLog takeActivityFromCache() {
        UserActivityLog userActivityLog = null;
        try {
            userActivityLog = queueUserActivity.poll(applicationProperties.getUserActivity().getEmptyQueueWaitingTime(), TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return userActivityLog;
    }
}
