package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.ExternalUrlTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUrlTrackingRepository extends JpaRepository<ExternalUrlTracking, Long> {}
