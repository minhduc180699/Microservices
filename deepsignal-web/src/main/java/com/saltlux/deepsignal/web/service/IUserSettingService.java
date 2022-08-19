package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.UserSetting;

public interface IUserSettingService {
    UserSetting saveUserSetting(UserSetting userSetting, String connectomeId) throws Exception;
    UserSetting getUserSettingByUserId(String connectomeId);
}
