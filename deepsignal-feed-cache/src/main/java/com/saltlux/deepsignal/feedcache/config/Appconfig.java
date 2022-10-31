package com.saltlux.deepsignal.feedcache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "appconfig")
@RefreshScope
@Data
public class Appconfig {
    private Redis redis;
    private Searcher searcher;
    private Kafka kafka;
    @Data
    public static class Redis {
        private String uri;
        private String password;
        private String feedPrefixCache;
        private String feedContentPrefixCache;
        private Long expiredTime;
    }
    @Data
    public static class Searcher {
        private String  serviceName;
        private Integer port;
    }
    @Data
    public static class Kafka {
        private String brokers;
        private String topic;
        private String group;
    }
}