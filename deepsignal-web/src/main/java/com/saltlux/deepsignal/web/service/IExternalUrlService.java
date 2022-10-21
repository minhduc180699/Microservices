package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.ExternalUrl;
import com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO;
import java.util.List;
import java.util.Optional;

public interface IExternalUrlService extends GeneralService<ExternalUrl, Long> {
    int countByOriginalUrl(String originalUrl);

    List<UrlTrackingDTO> countMostClickedUrl(int limit);

    String createShortUrl(String originalUrl);

    Optional<ExternalUrl> findByOriginalUrl(String originalUrl);

    Optional<ExternalUrl> findByShortUrl(String shortUrl);
}
