package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.ExternalUrlTracking;
import com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO;
import java.util.List;

public interface IExternalUrlTrackingService extends GeneralService<ExternalUrlTracking, Long> {
    int countByOriginalUrl(String originalUrl);

    List<UrlTrackingDTO> countMostClickedUrl(int limit);
}
