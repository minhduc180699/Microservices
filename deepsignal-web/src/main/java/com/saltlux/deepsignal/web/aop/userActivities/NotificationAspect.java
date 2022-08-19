package com.saltlux.deepsignal.web.aop.userActivities;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.api.websocket.dto.NotificationDTO;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Comment;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.domain.UserActivityLog;
import com.saltlux.deepsignal.web.factory.NotificationTypeFactory;
import com.saltlux.deepsignal.web.factory.NotificationTypeParent;
import com.saltlux.deepsignal.web.repository.UserActivityLogRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.INotificationService;
import com.saltlux.deepsignal.web.service.dto.UserDTO;
import com.saltlux.deepsignal.web.service.mapper.NotificationMapper;
import com.saltlux.deepsignal.web.util.HttpRequestResponseUtils;
import com.saltlux.deepsignal.web.util.Utils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class NotificationAspect {

    private UserRepository userRepository;
    private INotificationService notificationService;
    private UserActivityLogRepository userActivityLogRepository;

    public NotificationAspect(
        UserRepository userRepository,
        INotificationService notificationService,
        UserActivityLogRepository userActivityLogRepository
    ) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.userActivityLogRepository = userActivityLogRepository;
    }

    @Before("@annotation(userActivity)")
    public void saveNotificationUser(JoinPoint joinPoint, UserActivity userActivity) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] paramNames = methodSignature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        Map<String, Object> mapParams = IntStream
            .range(0, paramNames.length)
            .collect(HashMap::new, (m, i) -> m.put(String.valueOf(paramNames[i]), paramValues[i]), Map::putAll);
        switch (userActivity.activityName()) {
            case Constants.UserActivities.COMMENT:
                Comment comment = (Comment) mapParams.get("comment");
                if (null != comment.getParentCommentId()) {
                    saveNotification(Constants.NotificationName.REPLY_COMMENT, comment);
                } else {
                    saveNotification(Constants.NotificationName.COMMENT_FEED, comment);
                }
                break;
            default:
                break;
        }
    }

    private void saveNotification(Constants.NotificationName notificationName, Comment comment) {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (!user.isPresent()) {
            throw new BadRequestException("User not exist!");
        }
        String urlTitle = HttpRequestResponseUtils.getHeader(Constants.Header.TITLE);
        String url = HttpRequestResponseUtils.getBaseRefererPage();
        List<String> receiverIds = findCommentOnPostAlso(comment, user.get().getId());
        if (receiverIds.size() > 0) {
            UserDTO userDTO = new UserDTO(user.get());
            notificationService.saveNotificationWithInterpolationType(
                receiverIds,
                notificationName,
                user.get(),
                url,
                Utils.appendStyle(userDTO.getFullName()),
                Utils.appendStyle(urlTitle)
            );
        }
    }

    // return list userId that is receiver
    private List<String> findCommentOnPostAlso(Comment comment, String senderId) {
        List<String> userReceiversRelated;
        List<UserActivityLog> userActivityLogs = userActivityLogRepository.findAllByFeedIdAndActivityName(
            comment.getFeedId(),
            Constants.UserActivities.COMMENT
        );
        List<String> usernames = userActivityLogs.stream().map(UserActivityLog::getUsername).collect(Collectors.toList());
        List<User> users = new ArrayList<>();
        for (String username : usernames) {
            Optional<User> userOptional = userRepository.findOneByLogin(username);
            userOptional.ifPresent(users::add);
        }
        userReceiversRelated = users.stream().map(User::getId).collect(Collectors.toList());
        // sender never send notification to them self, so remove sender from receiver list
        if (!userReceiversRelated.isEmpty()) {
            userReceiversRelated.remove(senderId);
        }
        return userReceiversRelated;
    }
}
