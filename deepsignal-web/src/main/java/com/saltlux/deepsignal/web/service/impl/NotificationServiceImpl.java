package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.api.websocket.dto.MessageWrapper;
import com.saltlux.deepsignal.web.api.websocket.dto.NotificationDTO;
import com.saltlux.deepsignal.web.concurrence.NotificationTask;
import com.saltlux.deepsignal.web.concurrence.RabbitMQTask;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Authority;
import com.saltlux.deepsignal.web.domain.Notification;
import com.saltlux.deepsignal.web.domain.NotificationType;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.factory.NotificationTypeFactory;
import com.saltlux.deepsignal.web.factory.NotificationTypeParent;
import com.saltlux.deepsignal.web.repository.NotificationRepository;
import com.saltlux.deepsignal.web.repository.NotificationTypeRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.AuthoritiesConstants;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.INotificationService;
import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.service.dto.UserDTO;
import com.saltlux.deepsignal.web.service.mapper.NotificationMapper;
import io.netty.util.internal.StringUtil;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ApplicationContext applicationContext;
    private final NotificationTypeRepository notificationTypeRepository;
    private final ApplicationProperties applicationProperties;
    private final UserService userService;

    public NotificationServiceImpl(
        UserRepository userRepository,
        NotificationRepository notificationRepository,
        NotificationMapper notificationMapper,
        ApplicationContext applicationContext,
        NotificationTypeRepository notificationTypeRepository,
        ApplicationProperties applicationProperties,
        UserService userService
    ) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.applicationContext = applicationContext;
        this.notificationTypeRepository = notificationTypeRepository;
        this.applicationProperties = applicationProperties;
        this.userService = userService;
    }

    @Override
    public void saveNotificationByAdminWithBasicType(List<String> receiversId, Constants.NotificationName notificationName) {
        List<User> users = userRepository.findAllByAuthoritiesIs(new Authority(AuthoritiesConstants.ADMIN));
        if (!users.isEmpty()) {
            saveNotificationWithBasicType(receiversId, notificationName, users.get(0));
        }
    }

    @Override
    public void saveNotificationWithBasicType(List<String> receiversId, Constants.NotificationName notificationName, User sender) {
        if (receiversId.size() == 0) {
            return;
        }
        try {
            NotificationDTO notificationDTO = new NotificationDTO();
            Constants.NotificationCategory notificationCategory = Constants.NotificationCategory.basic;
            notificationDTO.setUserDTO(new UserDTO(sender));
            NotificationTypeParent notificationTypeParent = NotificationTypeFactory.getTemplateNotification(
                notificationCategory,
                notificationName
            );
            notificationDTO.setUrl(notificationTypeParent.getNotificationUrl());
            notificationDTO.setContent(notificationTypeParent.getNotificationContent());
            notificationDTO.setTitle(notificationTypeParent.getNotificationTitle());
            notificationDTO.setBtnContent(notificationTypeParent.getNotificationBtn());
            notificationDTO.setIconContent(notificationTypeParent.getNotificationIcon());
            notificationDTO.setContentI18n(notificationTypeParent.getTemplateContentI18n());
            notificationDTO.setTitleI18n(notificationTypeParent.getTemplateTitleI18n());
            notificationDTO.setCategory(notificationCategory);
            notificationDTO.setTypeName(notificationName.name);
            notificationDTO.setReceiverIds(receiversId);
            this.sendNotification(notificationDTO);
        } catch (Exception e) {
            log.error("Error when save new notification, message {}: ", e.getMessage());
        }
    }

    @Override
    public void saveNotificationByAdminWithInterpolationType(
        List<String> receiversId,
        Constants.NotificationName notificationName,
        String url,
        String... valInterpolation
    ) {
        List<User> users = userRepository.findAllByAuthoritiesIs(new Authority(AuthoritiesConstants.ADMIN));
        if (!users.isEmpty()) {
            saveNotificationWithInterpolationType(receiversId, notificationName, users.get(0), url, valInterpolation);
        }
    }

    @Override
    public void saveNotificationWithInterpolationType(
        List<String> receiversId,
        Constants.NotificationName notificationName,
        User sender,
        String url,
        String... valInterpolation
    ) {
        if (receiversId.size() == 0) {
            return;
        }
        try {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setUserDTO(new UserDTO(sender));
            notificationDTO.setCategory(Constants.NotificationCategory.interpolation);
            notificationDTO.setTypeName(notificationName.name);
            NotificationTypeParent notificationTypeParent = NotificationTypeFactory.getTemplateNotification(
                Constants.NotificationCategory.interpolation,
                notificationName
            );
            notificationDTO.setUrl(!StringUtil.isNullOrEmpty(url) ? url : notificationTypeParent.getNotificationUrl());
            notificationDTO.setTitle(notificationTypeParent.getNotificationTitle());
            notificationDTO.setContent(notificationTypeParent.getNotificationContent(valInterpolation));
            notificationDTO.setBtnContent(notificationTypeParent.getNotificationBtn());
            notificationDTO.setIconContent(notificationTypeParent.getNotificationIcon());
            notificationDTO.setContentI18n(notificationTypeParent.getTemplateContentI18n());
            notificationDTO.setTitleI18n(notificationTypeParent.getTemplateTitleI18n());
            notificationDTO.setReceiverIds(receiversId);
            this.sendNotification(notificationDTO);
        } catch (Exception e) {
            log.error("Error when save new notification, message: {}", e.getMessage());
        }
    }

    // After save notification commit success to database, one notification will push to tracker by websocket
    @Override
    public void sendNotification(Notification notification) throws InterruptedException {
        NotificationTask notificationTask = applicationContext.getBean(NotificationTask.class);
        notification.setUser(Objects.nonNull(notification.getUser()) ? notification.getUser() : getCurrentUser());
        notificationTask.setNotification(notification);
        Thread threadSaveNotification = new Thread(notificationTask);
        threadSaveNotification.start();
        log.info("Thread save notification running");
        threadSaveNotification.join();
        //        User user = Objects.nonNull(notification.getUser()) ? notification.getUser() : getCurrentUser();
        this.sendDataByWebsocket(new MessageWrapper("/notification", notification.getReceiverIds()));
    }

    @Override
    public void sendNotification(NotificationDTO notificationDTO) throws InterruptedException {
        sendNotification(notificationMapper.notificationDTOToNotification(notificationDTO));
    }

    @Override
    @Async
    public void sendDataByWebsocket(MessageWrapper messageWrapper) {
        try {
            RabbitMQTask rabbitMQTask = applicationContext.getBean(RabbitMQTask.class);
            rabbitMQTask.setURL_MESSAGE(messageWrapper.getUrlMessage());
            rabbitMQTask.setObject(messageWrapper.getData());
            rabbitMQTask.setExchangeName(applicationProperties.getRabbitConfig().getNotification().getExchangeName());
            rabbitMQTask.setRouterKey(applicationProperties.getRabbitConfig().getNotification().getRouterKey());
            Thread threadPushNotification = new Thread(rabbitMQTask);
            log.info("Thread send data to client by websocket start");
            threadPushNotification.start();
        } catch (Exception e) {
            log.error("Error when send data to client by websocket, cause: " + e.getMessage());
        }
    }

    @Override
    public Page<Notification> pagingNotification(String type, Pageable pageable) {
        User user = getCurrentUser();
        return StringUtils.isNotBlank(type)
            ? notificationRepository.findAllByReceiverIdAndType(user.getId(), type, pageable)
            : notificationRepository.findAllByReceiverId(user.getId(), pageable);
    }

    @Override
    @Transactional
    public boolean markAllRead() {
        User user = getCurrentUser();
        notificationRepository.markAllReadByUser(Notification.MARK_READ.READ, user.getId());
        return true;
    }

    @Override
    @Transactional
    public void markAllCheck() {
        User user = getCurrentUser();
        notificationRepository.markAllCheckedByUser(Notification.MARK_READ.READ, user.getId());
    }

    @Override
    @Transactional
    public boolean markAsRead(Long id) {
        notificationRepository.markAllReadByUserAndId(Notification.MARK_READ.READ, id);
        // if user clicked to notification, that read and checked
        notificationRepository.markAllCheckedByUserAndId(Notification.MARK_READ.READ, id);
        return true;
    }

    @Override
    public long countAllUnRead() {
        User user = getCurrentUser();
        return notificationRepository.countByIsCheckedAndUser(user.getId(), Notification.MARK_READ.UNREAD);
    }

    @Override
    public Set<String> getAllNotificationType() {
        List<NotificationType> notificationTypes = notificationTypeRepository.findAll();
        return notificationTypes.stream().map(NotificationType::getType).collect(Collectors.toSet());
    }

    @Override
    public void deleteReadNotification() {
        User user = getCurrentUser();
        List<Notification> notifications = notificationRepository.findAllByReceiverIdAndIsMarkedReadAndIsChecked(
            user.getId(),
            Notification.MARK_READ.READ,
            Notification.MARK_READ.READ
        );
        notificationRepository.deleteAll(notifications);
    }

    @Override
    public void deleteAllNotification() {
        User user = getCurrentUser();
        List<Notification> notifications = notificationRepository.findAllByReceiverId(user.getId());
        notificationRepository.deleteAll(notifications);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        notification.ifPresent(notificationRepository::delete);
    }

    private User getCurrentUser() {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (!user.isPresent()) {
            throw new BadRequestException("User not exist!");
        }
        return user.get();
    }
}
