package com.saltlux.deepsignal.web.service.mapper;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.api.websocket.dto.NotificationDTO;
import com.saltlux.deepsignal.web.domain.Notification;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.NotificationTypeRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link com.saltlux.deepsignal.web.domain.Notification} and its DTO called {@link com.saltlux.deepsignal.web.api.websocket.dto.NotificationDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class NotificationMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    public Notification notificationDTOToNotification(NotificationDTO notificationDTO) {
        if (notificationDTO == null) {
            return null;
        }
        Notification notification = new Notification();
        if (Objects.nonNull(notificationDTO.getUserDTO())) {
            Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(notificationDTO.getUserDTO().getLogin());
            if (!user.isPresent()) {
                throw new BadRequestException("Save Failed! User not exist");
            }
            notification.setUser(user.get());
        }
        notification.setUrl(notificationDTO.getUrl());
        notification.setContent(notificationDTO.getContent());
        notification.setIsMarkedRead(notificationDTO.getIsMarkedRead());
        notification.setTitle(notificationDTO.getTitle());
        notification.setBtnContent(notificationDTO.getBtnContent());
        notification.setIconContent(notificationDTO.getIconContent());
        notification.setCreatedDate(notificationDTO.getCreatedDate());
        notification.setReceiverIds(notificationDTO.getReceiverIds());
        notification.setContentI18n(notificationDTO.getContentI18n());
        notification.setTitleI18n(notificationDTO.getTitleI18n());
        notification.setNotificationType(
            notificationTypeRepository.getByCategoryAndName(notificationDTO.getCategory(), notificationDTO.getTypeName())
        );
        return notification;
    }

    public NotificationDTO notificationToNotificationDTO(Notification notification) {
        return new NotificationDTO(notification);
    }
}
