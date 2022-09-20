package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.ExternalUrlTracking;
import com.saltlux.deepsignal.web.repository.ExternalUrlTrackingRepository;
import com.saltlux.deepsignal.web.service.IExternalUrlTrackingService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExternalUrlTrackingServiceImpl implements IExternalUrlTrackingService {

    private final ExternalUrlTrackingRepository externalUrlTrackingRepository;

    public ExternalUrlTrackingServiceImpl(ExternalUrlTrackingRepository externalUrlTrackingRepository) {
        this.externalUrlTrackingRepository = externalUrlTrackingRepository;
    }

    @Override
    public List<ExternalUrlTracking> findAll() {
        return null;
    }

    @Override
    public Optional<ExternalUrlTracking> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public ExternalUrlTracking save(ExternalUrlTracking externalUrlTracking) {
        return externalUrlTrackingRepository.save(externalUrlTracking);
    }

    @Override
    public void remove(Long aLong) {}
}
