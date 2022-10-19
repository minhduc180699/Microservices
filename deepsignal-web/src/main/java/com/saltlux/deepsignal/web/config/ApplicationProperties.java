package com.saltlux.deepsignal.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Deep Signal.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Data
public class ApplicationProperties {

    private final Security security = new Security();

    private final FilesUpload filesUpload = new FilesUpload();

    private final ExternalApi externalApi = new ExternalApi();

    private final Twilio twilio = new Twilio();

    private final UserActivity userActivity = new UserActivity();

    private final RabbitConfig rabbitConfig = new RabbitConfig();

    private final Redis redis = new Redis();

    @Data
    public static class Security {

        private final Jwt jwt = new Jwt();
        private final Login login = new Login();

        @Data
        public static class Jwt {

            private String tokenIssuer;
            private String tokenAudience;
            private Integer tokenRefreshLimit;
        }

        @Data
        public static class Login {

            private Integer maxFailedLogin;
            private Long lockTimeDuration;
        }
    }

    @Data
    public static class FilesUpload {

        private String location;
        private String maxSize;
        private String urlFile;
    }

    @Data
    public static class ExternalApi {

        private String deepsignalAdapter;
        private String deepsignalOutside;
        private String deepsignalConnectome;
        private String deepsignalConnectomeTest;
        private String deepsignalRecommendationTest;
        private String deepsignalEntityNetwork;
        private String deepsignalFile;
        private String deepsignalStock;
        private String deepsignalToolbox;
        private String deepsignalDashboard;
        private String deepsignalMetasearch;
        private String deepsignalDocConverter;
        private String deepsignalIssueTracking;
        private String deepsignalTrendTimeseries;
        private String deepsignalRelatedContent;
    }

    @Data
    public static class Twilio {

        private String accountSid;
        private String authToken;
        private String senderNumber;
        private int lengthCode = 6;
        private int expiredTime = 3;
    }

    @Data
    public static class UserActivity {

        private int numberThreadConsumer;
        private int fullQueueWaitingTime;
        private int emptyQueueWaitingTime;
    }

    @Data
    public static class RabbitConfig {

        private BaseRabbitConfig notification;
        private BaseRabbitConfig webLogging;
        private BaseRabbitConfig feedTraining;
        private BaseRabbitConfig connectomeTraining;
        private BaseRabbitConfig signalTracking;

        @Data
        public static class BaseRabbitConfig {

            private String exchangeName;
            private String queueName;
            private String routerKey;
        }
    }

    @Data
    public static class Redis {

        private String type;
        private String username;
        private String password;
        private SingleCache singleCache;
        private MasterSlaveCache masterSlaveCache;
        private ClusterCache clusterCache;

        @Data
        public static class SingleCache {

            private String address;
        }

        @Data
        public static class MasterSlaveCache {

            private String masterAddress;
            private String slaveAddress;
        }

        @Data
        public static class ClusterCache {

            private String address;
        }
    }
}
