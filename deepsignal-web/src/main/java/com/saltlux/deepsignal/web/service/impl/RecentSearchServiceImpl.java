package com.saltlux.deepsignal.web.service.impl;

import static com.saltlux.deepsignal.web.domain.AbstractBaseEntity.SORT_BY_LAST_MODIFY_DATE_DESC;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.domain.RecentSearch;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.IRecentSearchRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.service.IRecentSearchService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecentSearchServiceImpl implements IRecentSearchService {

    private final IRecentSearchRepository recentSearchRepository;
    private final UserRepository userRepository;

    public RecentSearchServiceImpl(IRecentSearchRepository recentSearchRepository, UserRepository userRepository) {
        this.recentSearchRepository = recentSearchRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<RecentSearch> pagingByContentAndUserId(Pageable pageable, String userId, String content) {
        return recentSearchRepository.findAllByContentContainingAndUser_Id(pageable, content, userId);
    }

    @Override
    public List<RecentSearch> getAllByContentAndUserId(String userId) {
        return recentSearchRepository.findAllByUser_Id(SORT_BY_LAST_MODIFY_DATE_DESC, userId);
    }

    @Override
    @Transactional
    public RecentSearch save(RecentSearch recentSearch) {
        if (StringHelper.isNullOrEmptyString(recentSearch.getContent()) || Objects.isNull(recentSearch.getUserId())) {
            throw new BadRequestException("Save fail. Content and UserId not null");
        }
        User user = userRepository.getOne(recentSearch.getUserId());
        recentSearch.setUser(user);
        Optional<RecentSearch> recentSearchOptional = recentSearchRepository.findByContentAndUser_Id(
            recentSearch.getContent(),
            recentSearch.getUserId()
        );
        if (recentSearchOptional.isPresent()) {
            recentSearchOptional.get().setLastModifiedDate(Instant.now());
            return recentSearchRepository.save(recentSearchOptional.get());
        }
        return recentSearchRepository.save(recentSearch);
    }

    @Override
    public void deleteById(Long id) {
        if (Objects.isNull(id)) {
            throw new BadRequestException("Delete fail. Id must not be null");
        }
        recentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByUserId(String userId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("Delete fail. userId must not be null");
        }
        recentSearchRepository.deleteAllByUserId(userId);
    }
}
