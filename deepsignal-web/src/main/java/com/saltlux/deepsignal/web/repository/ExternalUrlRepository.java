package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.ExternalUrl;
import com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUrlRepository extends JpaRepository<ExternalUrl, Long> {
    @Query(
        value = "SELECT SUM(tracking.click) " +
        "FROM external_url url " +
        "JOIN user_url_tracking tracking " +
        "ON tracking.external_url_id = url.id " +
        "WHERE url.original_url = ?1 ",
        nativeQuery = true
    )
    int countByUrl(String url);

    @Query(
        value = "SELECT new com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO(eut.originalUrl, SUM(tracking.click)) " +
        "FROM ExternalUrl eut " +
        "JOIN UserUrlTracking tracking ON eut.id = tracking.externalUrlId " +
        "GROUP BY tracking.externalUrlId " +
        "ORDER BY SUM(tracking.click) " +
        "DESC "
    )
    List<UrlTrackingDTO> countMostClickedUrl(Pageable pageable);

    Optional<ExternalUrl> findByOriginalUrl(String originalUrl);

    Optional<ExternalUrl> findByShortUrl(String shortUrl);
}
