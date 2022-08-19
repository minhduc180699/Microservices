package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.WebSource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link WebSource} entity.
 */
@Repository
public interface ConnectomeRepository extends JpaRepository<Connectome, String> {
    Connectome findByConnectomeName(String connectomeName);
    List<Connectome> findByUser_Id(String userId);
    Optional<Connectome> findOneByUser_Id(String userId);
    void deleteByUser_Id(String userId);

    Optional<Connectome> findConnectomeByConnectomeId(String connectomeId);
}
