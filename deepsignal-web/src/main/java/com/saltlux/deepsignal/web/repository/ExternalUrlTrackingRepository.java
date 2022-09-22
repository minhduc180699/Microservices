package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.ExternalUrlTracking;
import com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUrlTrackingRepository extends JpaRepository<ExternalUrlTracking, Long> {
    @Query(
        value = "SELECT COUNT(original_url) " + "FROM external_url_tracking " + "WHERE original_url = ?1 " + "GROUP BY original_url",
        nativeQuery = true
    )
    int countByUrl(String url);

    @Query(
        value = "SELECT new com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO(eut.originalUrl, COUNT(eut.originalUrl)) " +
            "FROM ExternalUrlTracking eut " +
            "GROUP BY eut.originalUrl " +
            "ORDER BY COUNT(eut.originalUrl) DESC "
    )
    List<UrlTrackingDTO> countMostClickedUrl(Pageable pageable);
}
