package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Interaction;
import com.saltlux.deepsignal.web.domain.InteractionUser;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.InteractionUserRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.IInteractionService;
import com.saltlux.deepsignal.web.service.IInteractionUserService;
import com.saltlux.deepsignal.web.service.dto.InteractionStatisticDTO;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InteractionUserServiceImpl implements IInteractionUserService {

    private final InteractionUserRepository interactionUserRepository;
    private final UserRepository userRepository;
    private final IInteractionService interactionService;

    public InteractionUserServiceImpl(
        InteractionUserRepository interactionUserRepository,
        UserRepository userRepository,
        IInteractionService interactionService
    ) {
        this.interactionUserRepository = interactionUserRepository;
        this.userRepository = userRepository;
        this.interactionService = interactionService;
    }

    @Override
    @Transactional
    public InteractionUser save(String feedId, int typeInteraction) {
        User user = preProcessInteraction(feedId, typeInteraction);
        InteractionUser interactionUser = new InteractionUser();
        interactionUser.setUser(user);
        Interaction interaction = interactionService.saveIfNoneExist(typeInteraction);
        interactionUser.setInteraction(interaction);
        interactionUser.setFeedId(feedId);
        interactionUser.setType(interaction.getType());
        return interactionUserRepository.save(interactionUser);
    }

    @Override
    public InteractionStatisticDTO statisticInteraction(String feedId) throws Exception {
        if (!StringUtils.isNotBlank(feedId)) {
            throw new BadRequestException("feed_id blank or null");
        }
        long totalLike = interactionUserRepository.countByFeedIdAndType(feedId, Constants.TypeInteraction.LIKE.type);
        long totalDislike = interactionUserRepository.countByFeedIdAndType(feedId, Constants.TypeInteraction.DISLIKE.type);
        long totalShare = interactionUserRepository.countByFeedIdAndType(feedId, Constants.TypeInteraction.SHARE.type);
        long totalBookmark = interactionUserRepository.countByFeedIdAndType(feedId, Constants.TypeInteraction.BOOKMARK.type);
        long totalComment = interactionUserRepository.countByFeedIdAndType(feedId, Constants.TypeInteraction.COMMENT.type);
        return new InteractionStatisticDTO(totalLike, totalDislike, totalComment, totalShare, totalBookmark);
    }

    @Override
    public boolean delete(String feedId, int typeInteraction) throws Exception {
        User user = preProcessInteraction(feedId, typeInteraction);
        deleteInteractionIfExist(feedId, typeInteraction, user.getId());
        return true;
    }

    @Override
    @Transactional
    public void updateType() {
        List<InteractionUser> interactionUsers = (List<InteractionUser>) interactionUserRepository.findAll();
        interactionUsers.forEach(
            interactionUser -> {
                interactionUser.setType(interactionUser.getInteraction().getType());
            }
        );
        interactionUserRepository.saveAll(interactionUsers);
    }

    private User preProcessInteraction(String feedId, int typeInteraction) {
        if (!StringUtils.isNotBlank(feedId) || typeInteraction == 0) {
            throw new BadRequestException("Save Failed! feed_id blank or type interaction not found");
        }
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (!user.isPresent()) {
            throw new BadRequestException("Save Failed! User not exist");
        }
        return user.get();
    }

    private void deleteInteractionIfExist(String feedId, int type, String userId) {
        Optional<InteractionUser> interactionUser = interactionUserRepository.findByFeedIdAndTypeAndUser_id(feedId, type, userId);
        interactionUser.ifPresent(interactionUserRepository::delete);
    }
}
