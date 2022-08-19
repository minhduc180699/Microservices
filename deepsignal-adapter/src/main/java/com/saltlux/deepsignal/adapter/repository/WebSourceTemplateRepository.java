package com.saltlux.deepsignal.adapter.repository;

import com.saltlux.deepsignal.adapter.domain.WebSourceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link WebSourceTemplate} entity.
 */
@Repository
public interface WebSourceTemplateRepository extends JpaRepository<WebSourceTemplate, String> {}
