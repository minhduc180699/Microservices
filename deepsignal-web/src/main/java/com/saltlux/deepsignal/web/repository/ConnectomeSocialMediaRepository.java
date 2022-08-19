package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.ConnectomeSocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link ConnectomeSocialMedia} entity.
 */
@Repository
public interface ConnectomeSocialMediaRepository extends JpaRepository<ConnectomeSocialMedia, Long> {}
