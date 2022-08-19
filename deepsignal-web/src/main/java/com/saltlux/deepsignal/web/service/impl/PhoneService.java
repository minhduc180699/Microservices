package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.api.errors.ErrorConstants;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.exception.AccountAlreadyExistException;
import com.saltlux.deepsignal.web.exception.AccountAlreadyNotExistException;
import com.saltlux.deepsignal.web.exception.BadRequestException;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.service.IPhoneService;
import com.saltlux.deepsignal.web.service.dto.AccountDTO;
import com.saltlux.deepsignal.web.service.dto.PhoneCodeDTO;
import com.saltlux.deepsignal.web.util.TwilioFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Status;

@Service
public class PhoneService implements IPhoneService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneService.class);

    private ApplicationProperties appProperties;

    private TwilioFactory twilioFactory;

    private RMapCache<String, String> mapSmsCode;

    @Autowired
    private UserRepository userRepository;

    public PhoneService(ApplicationProperties appProperties, TwilioFactory twilioFactory, RMapCache<String, String> mapSmsCode) {
        this.appProperties = appProperties;
        this.twilioFactory = twilioFactory;
        this.mapSmsCode = mapSmsCode;
    }

    @Override
    public PhoneCodeDTO sendSMS(PhoneCodeDTO phoneCodeDTO) {
        if (Constants.SEND_CODE_LOGIN_ACTION.equals(phoneCodeDTO.getType())) {
            Optional<User> user = userRepository.findOneByLogin(phoneCodeDTO.getLogin());
            if (!user.isPresent()) {
                throw new AccountAlreadyNotExistException();
            }
        }

        String code = createSmsCode();
        if (code == null) {
            return null;
        } else {
            Boolean check = twilioFactory.sendSMS(phoneCodeDTO.getPhoneNumber(), phoneCodeDTO.getCountryCode(), code);
            if (check) {
                mapSmsCode.put(
                    phoneCodeDTO.getCountryCode() + phoneCodeDTO.getPhoneNumber(),
                    code,
                    appProperties.getTwilio().getExpiredTime(),
                    TimeUnit.MINUTES
                );
                // For test environment with phone number is: 5571981265131
                if (Constants.PHONE_NUMBER_TEST.equals(phoneCodeDTO.getPhoneNumber())) {
                    phoneCodeDTO.setCode(code);
                }
            } else {
                return null;
            }
        }
        return phoneCodeDTO;
    }

    @Override
    public Constants.ErrorCode verifyCode(AccountDTO accountDTO) {
        if (validateCode(accountDTO) != null) {
            return validateCode(accountDTO);
        }
        mapSmsCode.remove(accountDTO.getCountryCode() + accountDTO.getPhoneNumber());
        return null;
    }

    private Constants.ErrorCode validateCode(AccountDTO accountDTO) {
        String codeInSession = mapSmsCode.get(accountDTO.getCountryCode() + accountDTO.getPhoneNumber()) != null
            ? mapSmsCode.get(accountDTO.getCountryCode() + accountDTO.getPhoneNumber())
            : null;
        if (StringUtils.isBlank(accountDTO.getCode())) {
            return Constants.ErrorCode.DEEPSINAL_VERIFY_CODE_BLANK;
        }
        if (StringUtils.isEmpty(codeInSession)) {
            return Constants.ErrorCode.DEEPSINAL_VERIFY_CODE_EXPIRED;
        }
        if (!StringUtils.equalsIgnoreCase(codeInSession, accountDTO.getCode())) {
            return Constants.ErrorCode.DEEPSINAL_VERIFY_CODE_INCORRECT;
        }
        return null;
    }

    private String createSmsCode() {
        StringBuilder generatedOTP = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");

            for (int i = 0; i < appProperties.getTwilio().getLengthCode(); i++) {
                generatedOTP.append(secureRandom.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return generatedOTP.toString();
    }
}
