package com.saltlux.deepsignal.web.util;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.service.impl.PhoneService;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.lookups.v1.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TwilioFactory {

    private static final Logger logger = LoggerFactory.getLogger(PhoneService.class);

    private ApplicationProperties appProperties;

    public TwilioFactory(ApplicationProperties appProperties) {
        this.appProperties = appProperties;
    }

    public boolean sendSMS(String phoneNumber, String countryCode, String code) {
        try {
            com.twilio.type.PhoneNumber to;
            // For test environment with phone number is: 5571981265131
            if (Constants.PHONE_NUMBER_TEST.equals(phoneNumber)) {
                to = new com.twilio.type.PhoneNumber(phoneNumber);
            } else {
                to = PhoneNumber.fetcher(new com.twilio.type.PhoneNumber(phoneNumber)).setCountryCode(countryCode).fetch().getPhoneNumber();
            }
            com.twilio.type.PhoneNumber from = new com.twilio.type.PhoneNumber(appProperties.getTwilio().getSenderNumber());
            Message message = Message.creator(to, from, code).create();
            logger.info("Send sms {}", message.getSid());
        } catch (TwilioException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
