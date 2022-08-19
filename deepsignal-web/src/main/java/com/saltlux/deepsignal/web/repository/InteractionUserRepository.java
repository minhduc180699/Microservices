package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.InteractionUser;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractionUserRepository extends PagingAndSortingRepository<InteractionUser, Long> {
    long countByFeedIdAndType(String feedId, int type);

    Optional<InteractionUser> findByFeedIdAndTypeAndUser_id(String feedId, int type, String userId);
}
