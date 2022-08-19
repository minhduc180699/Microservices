package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Notification;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {
    @Query(
        value = "SELECT n FROM Notification AS n INNER JOIN NotificationReceiver AS nr ON n.id = nr.notification.id AND nr.receiver.id = ?1 AND n.type = ?2"
    )
    Page<Notification> findAllByReceiverIdAndType(String userId, String type, Pageable pageable);

    @Query(
        value = "SELECT n FROM Notification AS n INNER JOIN NotificationReceiver AS nr ON n.id = nr.notification.id AND nr.receiver.id = ?1"
    )
    Page<Notification> findAllByReceiverId(String userId, Pageable pageable);

    @Query(
        value = "SELECT n FROM Notification AS n INNER JOIN NotificationReceiver AS nr ON n.id = nr.notification.id AND nr.receiver.id = ?1"
    )
    List<Notification> findAllByReceiverId(String userId);

    @Query(
        value = "SELECT n FROM Notification AS n INNER JOIN NotificationReceiver AS nr ON n.id = nr.notification.id" +
        " AND nr.receiver.id = ?1 AND n.isMarkedRead = ?2 AND n.isChecked = ?3"
    )
    List<Notification> findAllByReceiverIdAndIsMarkedReadAndIsChecked(String receiverId, Integer isMarkRead, Integer isChecked);

    @Modifying
    @Query(
        "UPDATE Notification n SET n.isMarkedRead = :markAllRead WHERE" +
        " n.id IN (SELECT nr.notification.id FROM NotificationReceiver AS nr WHERE nr.receiver.id = :userId)"
    )
    void markAllReadByUser(@Param("markAllRead") Integer markAllRead, @Param("userId") String userId);

    @Modifying
    @Query(
        "UPDATE Notification n SET n.isChecked = :markAllChecked WHERE" +
        " n.id IN (SELECT nr.notification.id FROM NotificationReceiver AS nr WHERE nr.receiver.id = :userId)"
    )
    void markAllCheckedByUser(@Param("markAllChecked") Integer markAllRead, @Param("userId") String userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isMarkedRead = :markRead WHERE n.id = :id")
    void markAllReadByUserAndId(@Param("markRead") Integer markAllRead, @Param("id") Long id);

    @Modifying
    @Query("UPDATE Notification n SET n.isChecked = :markCheck WHERE n.id = :id")
    void markAllCheckedByUserAndId(@Param("markCheck") Integer markAllCheck, @Param("id") Long id);

    @Query(
        value = "SELECT COUNT(n) FROM Notification As n INNER JOIN NotificationReceiver AS nr" +
        " ON n.id = nr.notification.id AND nr.receiver.id = ?1 AND n.isChecked = ?2"
    )
    long countByIsCheckedAndUser(String userId, Integer markRead);
}
