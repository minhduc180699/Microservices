package com.saltlux.deepsignal.adapter.repository;

import com.saltlux.deepsignal.adapter.domain.WebSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link WebSource} entity.
 */
@Repository
public interface WebSourceRepository extends JpaRepository<WebSource, Long> {
    boolean existsByWebUrl(String webUrl);
}
