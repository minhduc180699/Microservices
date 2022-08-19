package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, String> {
    @Query("SELECT p from Purpose p WHERE p.purposeName = ?1")
    Optional<Purpose> findOneByPurposeName(String purposeName);
}
