package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.UserUrlTracking;
import com.saltlux.deepsignal.web.domain.compositekey.UserUrlTrackingId;
import java.util.Optional;

public interface IUserUrlTrackingService {
    Optional<UserUrlTracking> findById(UserUrlTrackingId userUrlTrackingId);

    UserUrlTracking save(UserUrlTracking userUrlTracking);
}
