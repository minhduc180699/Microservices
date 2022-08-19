package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.WebSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link WebSource} entity.
 */
@Repository
public interface WebSourceRepository extends JpaRepository<WebSource, String> {
    boolean existsByWebUrl(String webUrl);
}
