package com.ds.dssearcher.runtime_config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "appconfig")
@Data
public class AppConfig {
    private Elasticsearch elasticsearch;
    @Data
    public static class Elasticsearch {
        private String nodes;
    }
}
