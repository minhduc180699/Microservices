package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.UserUrlTracking;
import com.saltlux.deepsignal.web.domain.compositekey.UserUrlTrackingId;
import com.saltlux.deepsignal.web.repository.UserUrlTrackingRepository;
import com.saltlux.deepsignal.web.service.IUserUrlTrackingService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserUrlTrackingServiceImpl implements IUserUrlTrackingService {

    private final UserUrlTrackingRepository userUrlTrackingRepository;

    public UserUrlTrackingServiceImpl(UserUrlTrackingRepository userUrlTrackingRepository) {
        this.userUrlTrackingRepository = userUrlTrackingRepository;
    }

    @Override
    public Optional<UserUrlTracking> findById(UserUrlTrackingId userUrlTrackingId) {
        return userUrlTrackingRepository.findById(userUrlTrackingId);
    }

    @Override
    public UserUrlTracking save(UserUrlTracking userUrlTracking) {
        return userUrlTrackingRepository.save(userUrlTracking);
    }
}
