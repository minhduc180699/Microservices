package com.saltlux.deepsignal.web.concurrence;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.domain.Notification;
import com.saltlux.deepsignal.web.domain.NotificationReceiver;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.NotificationReceiverRepository;
import com.saltlux.deepsignal.web.repository.NotificationRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import java.time.Instant;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Log4j2
public class NotificationTask implements Runnable {

    @NotNull
    private Notification notification;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private UserRepository userRepository;

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @SneakyThrows
    @Override
    public void run() {
        if (Objects.nonNull(notification)) {
            saveNotification(notification);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveNotification(@Valid Notification notification) {
        notification.setIsMarkedRead(Notification.MARK_READ.UNREAD);
        notification.setCreatedDate(Instant.now());
        notification.setIsChecked(Notification.MARK_READ.UNREAD);

        // save notification receiver
        if (!notification.getReceiverIds().isEmpty()) {
            notification.setReceiverIds(removeSenderFromReceiver(notification.getUser().getId(), notification.getReceiverIds()));
            Set<NotificationReceiver> notificationReceivers = new HashSet<>();
            List<User> receivers = userRepository.findAllByIdInAndActivated(notification.getReceiverIds(), 1);
            if (receivers.size() > 0) {
                Notification notificationSaved = notificationRepository.save(notification);
                for (User user : receivers) {
                    NotificationReceiver notificationReceiver = new NotificationReceiver();
                    notificationReceiver.setReceiver(user);
                    notificationReceiver.setNotification(notificationSaved);
                    notificationReceivers.add(notificationReceiver);
                }
            }
            //            for (User user : receivers) {
            //                Optional<User> userOptional = userRepository.findByIdAndActivated(receiverId, 1);
            //                if (userOptional.isPresent()) {
            //                    NotificationReceiver notificationReceiver = new NotificationReceiver();
            //                    notificationReceiver.setReceiver(userOptional.get());
            //                    notificationReceiver.setNotification(notificationSaved);
            //                    notificationReceivers.add(notificationReceiver);
            //                }
            //            }
            notificationReceiverRepository.saveAll(notificationReceivers);
        } else {
            log.info("Save notification failed! Reason: Receivers is empty");
        }
    }

    private List<String> removeSenderFromReceiver(String senderId, List<String> receiverIds) {
        if (!receiverIds.isEmpty() && !receiverIds.contains(senderId)) {
            receiverIds.remove(senderId);
        }
        return receiverIds;
    }
}
