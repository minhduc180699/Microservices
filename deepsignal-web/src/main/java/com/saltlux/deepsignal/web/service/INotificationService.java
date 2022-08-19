package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.api.websocket.dto.MessageWrapper;
import com.saltlux.deepsignal.web.api.websocket.dto.NotificationDTO;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Notification;
import com.saltlux.deepsignal.web.domain.NotificationType;
import com.saltlux.deepsignal.web.domain.User;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

public interface INotificationService {
    /**
     * When use: save new notification with basic category and sender has role admin.
     *
     * @param receiversId list id of user receiver
     * @param notificationName name of notification category, just pass value from enum NotificationName. If it isn't exist, please add new value to enum NotificationName
     */
    void saveNotificationByAdminWithBasicType(@NotEmpty List<String> receiversId, Constants.NotificationName notificationName);

    /**
     * When use: save new notification with basic category.
     *
     * @param receiversId list id of user receiver
     * @param notificationName name of notification category, just pass value from enum NotificationName. If it isn't exist, please add new value to enum NotificationName
     * @param sender user is sender of this notification
     */
    void saveNotificationWithBasicType(@NotEmpty List<String> receiversId, Constants.NotificationName notificationName, User sender);

    /**
     * When use: save new notification with interpolation category and sender has role admin.
     *
     * @param receiversId list id of user receiver
     * @param notificationName name of notification category, just pass value from enum NotificationName. If it isn't exist, please add new value to enum NotificationName
     * @param url url of notification
     * @param valInterpolation value to pass to content of interpolation content
     */
    void saveNotificationByAdminWithInterpolationType(
        @NotEmpty List<String> receiversId,
        Constants.NotificationName notificationName,
        String url,
        String... valInterpolation
    );

    /**
     * When use: save new notification with interpolation category.
     *
     * @param receiversId list id of user receiver
     * @param notificationName name of notification category, just pass value from enum NotificationName. If it isn't exist, please add new value to enum NotificationName
     * @param sender user is sender of this notification
     * @param url url of notification
     * @param valInterpolation value to pass to content of interpolation content
     */
    void saveNotificationWithInterpolationType(
        @NotEmpty List<String> receiversId,
        Constants.NotificationName notificationName,
        User sender,
        String url,
        String... valInterpolation
    );

    // auto push notification when save
    void sendNotification(Notification notification) throws InterruptedException;
    void sendNotification(NotificationDTO notificationDTO) throws InterruptedException;

    @Async
    void sendDataByWebsocket(MessageWrapper messageWrapper);

    Page<Notification> pagingNotification(String type, Pageable pageable);
    boolean markAllRead();
    void markAllCheck();
    boolean markAsRead(Long id);
    long countAllUnRead();
    Set<String> getAllNotificationType();
    void deleteReadNotification();
    void deleteAllNotification();
    void deleteById(Long id);
}
