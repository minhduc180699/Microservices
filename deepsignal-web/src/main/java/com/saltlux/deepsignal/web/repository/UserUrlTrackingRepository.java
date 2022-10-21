package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.UserUrlTracking;
import com.saltlux.deepsignal.web.domain.compositekey.UserUrlTrackingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserUrlTrackingRepository extends JpaRepository<UserUrlTracking, UserUrlTrackingId> {}
