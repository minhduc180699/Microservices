package com.saltlux.deepsignal.web.config;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.http.TwilioRestClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioInitializer {

    private static final Logger logger = LoggerFactory.getLogger(TwilioInitializer.class);

    private ApplicationProperties appProperties;

    @Autowired
    public TwilioInitializer(ApplicationProperties appProperties) {
        this.appProperties = appProperties;
        if (StringUtils.isAnyBlank(appProperties.getTwilio().getAccountSid(), appProperties.getTwilio().getAuthToken())) {
            if (logger.isWarnEnabled()) {
                logger.warn("Twilio account-id or auth-token is blank");
            }
            return;
        }
        try {
            Twilio.init(appProperties.getTwilio().getAccountSid(), appProperties.getTwilio().getAuthToken());
            logger.info("Twilio initialized ... with account sid {} ", appProperties.getTwilio().getAccountSid());
        } catch (TwilioException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to initialize the Twilio environment", e);
            }
        }
    }

    @Bean
    public TwilioRestClient twilioRestClient() {
        return new TwilioRestClient.Builder(appProperties.getTwilio().getAccountSid(), appProperties.getTwilio().getAuthToken()).build();
    }
}
