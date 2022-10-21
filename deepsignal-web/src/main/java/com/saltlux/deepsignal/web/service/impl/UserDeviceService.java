package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.domain.UserDevice;
import com.saltlux.deepsignal.web.repository.UserDeviceRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.service.IUserDevice;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UserDeviceService implements IUserDevice {

    private final UserDeviceRepository userDeviceRepository;

    private final UserRepository userRepository;

    public UserDeviceService(UserDeviceRepository userDeviceRepository, UserRepository userRepository) {
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String saveUserDevice(String login, String deviceId) {
        String secretKey = generateSecretKy(login, deviceId);
        if (StringUtils.isNotEmpty(secretKey)) {
            Optional<User> optionalUser = userRepository.findOneByLogin(login);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                UserDevice userDevice = new UserDevice(deviceId, user, secretKey);
                userDeviceRepository.save(userDevice);
                return secretKey;
            }
        }
        return null;
    }

    public String generateSecretKy(String login, String deviceId) {
        String account = login + "-" + deviceId;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(account.getBytes());
            byte[] key = md.digest();
            BigInteger bigint = new BigInteger(1, key);
            String secretKey = bigint.toString(32);
            while (secretKey.length() < 32) {
                secretKey = "0" + secretKey;
            }
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
