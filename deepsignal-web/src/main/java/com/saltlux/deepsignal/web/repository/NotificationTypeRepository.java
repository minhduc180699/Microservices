package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.NotificationType;
import java.util.List;
import java.util.Optional;
import javax.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    NotificationType getByCategoryAndName(Constants.NotificationCategory category, String name);
    Optional<NotificationType> findByCategoryAndName(Constants.NotificationCategory category, String name);
    List<NotificationType> findAllByType(String type);
}
