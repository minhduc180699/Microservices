package com.saltlux.deepsignal.web.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.domain.UserSetting;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.repository.UserSettingRepository;
import com.saltlux.deepsignal.web.service.IUserSettingService;
import com.saltlux.deepsignal.web.service.WeatherService;
import com.saltlux.deepsignal.web.service.dto.WeatherSettingDto;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserSettingServiceImpl implements IUserSettingService {

    private UserSettingRepository userSettingRepository;
    private WeatherService weatherService;
    private final ConnectomeRepository connectomeRepository;

    UserSettingServiceImpl(
        UserSettingRepository userSettingRepository,
        WeatherService weatherService,
        ConnectomeRepository connectomeRepository
    ) {
        this.userSettingRepository = userSettingRepository;
        this.weatherService = weatherService;
        this.connectomeRepository = connectomeRepository;
    }

    @Override
    public UserSetting saveUserSetting(UserSetting userSetting, String connectomeId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        UserSetting existSetting = this.getUserSettingByUserId(connectomeId);
        if (existSetting != null) {
            WeatherSettingDto weatherSettingDto = objectMapper.readValue(userSetting.getWeather(), WeatherSettingDto.class);
            if (weatherSettingDto != null && weatherSettingDto.getAlwaysDetectLocation()) {
                existSetting.setLocWeather(userSetting.getLocWeather());
            }
            existSetting.setStock(userSetting.getStock());
            existSetting.setWeather(userSetting.getWeather());
            return userSettingRepository.save(existSetting);
        }
        Optional<Connectome> connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeId);
        if (!connectome.isPresent()) {
            return null;
        }
        User user = connectome.get().getUser();
        userSetting.setUserId(user.getId());
        return userSettingRepository.save(userSetting);
    }

    @Override
    public UserSetting getUserSettingByUserId(String connectomeId) {
        Optional<Connectome> connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeId);
        if (!connectome.isPresent()) {
            return null;
        }
        User user = connectome.get().getUser();
        return userSettingRepository.findUserSettingByUserId(user.getId());
    }
}
