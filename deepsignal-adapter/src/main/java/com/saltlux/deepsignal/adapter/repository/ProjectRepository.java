package com.saltlux.deepsignal.adapter.repository;

import com.saltlux.deepsignal.adapter.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Project} entity.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {}
