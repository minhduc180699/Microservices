package com.saltlux.deepsignal.adapter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Deep Signal Adapter.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Data
public class ApplicationProperties {

    private final KafkaCfg kafkaCfg = new KafkaCfg();

    private final ExternalApi externalApi = new ExternalApi();

    @Data
    public static class KafkaCfg {

        private String bootstrapServers;
        private String groupId;
        private String topicName;
        private String messagesPerRequest;
    }

    @Data
    public static class ExternalApi {

        private String bigoAPI;
    }
}
