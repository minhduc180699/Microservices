package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.api.websocket.dto.NotificationDTO;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Comment;
import com.saltlux.deepsignal.web.domain.Notification;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.CommentRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.ICommentService;
import com.saltlux.deepsignal.web.service.IInteractionUserService;
import com.saltlux.deepsignal.web.service.INotificationService;
import com.saltlux.deepsignal.web.service.dto.UserDTO;
import com.saltlux.deepsignal.web.service.mapper.NotificationMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IInteractionUserService interactionUserService;
    private final INotificationService notificationService;

    public CommentServiceImpl(
        CommentRepository commentRepository,
        UserRepository userRepository,
        IInteractionUserService interactionUserService,
        INotificationService notificationService
    ) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.interactionUserService = interactionUserService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public Comment save(Comment comment) throws Exception {
        if (!StringUtils.isNotBlank(comment.getContent())) {
            throw new BadRequestException("Save failed! Content is empty");
        }
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (!user.isPresent()) {
            throw new BadRequestException("Save Failed! User not exist");
        }
        if (Objects.nonNull(comment.getParentCommentId())) {
            Optional<Comment> commentParent = commentRepository.findById(comment.getParentCommentId());
            commentParent.ifPresent(comment::setComment);
        }
        comment.setUser(user.get());
        this.saveLogComment(comment.getFeedId());
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> findByFeed(Pageable pageable, String feedId) {
        return findAllCommentParentByFeed(pageable, feedId);
    }

    @Override
    public long countCommentByFeed(String feedId) {
        return commentRepository.countAllByFeedId(feedId);
    }

    private void saveLogComment(String feedId) throws Exception {
        this.interactionUserService.save(feedId, Constants.TypeInteraction.COMMENT.type);
    }

    private Page<Comment> findAllCommentParentByFeed(Pageable pageable, String feedId) {
        return commentRepository.findByFeedIdAndCommentIsNull(pageable, feedId);
    }
}
