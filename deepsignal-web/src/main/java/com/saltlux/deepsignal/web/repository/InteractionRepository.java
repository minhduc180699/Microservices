package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Interaction;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractionRepository extends PagingAndSortingRepository<Interaction, Long> {
    Optional<Interaction> findByType(int type);
}
