package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.UserDevice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice> findUserDeviceByDeviceIdAndUser_Id(String deviceId, String userId);
    Optional<UserDevice> findUserDeviceBySecretKey(String secretKey);
}
