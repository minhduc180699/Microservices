package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.NotificationReceiver;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationReceiverRepository extends PagingAndSortingRepository<NotificationReceiver, Long> {}
