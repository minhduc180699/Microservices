package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Integer> {
    UserSetting findUserSettingByUserId(String userId);
}
