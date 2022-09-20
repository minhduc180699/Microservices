package com.saltlux.deepsignal.web.service.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ExternalUrlTrackingDTO implements Serializable {

    private Long id;

    private String userId;

    private String connectomeId;

    private String url;

    private String originalUrl;

    private String title;

    private String createdDate;

    public ExternalUrlTrackingDTO(
        Long id,
        String userId,
        String connectomeId,
        String url,
        String originalUrl,
        String title,
        String createdDate
    ) {
        this.id = id;
        this.userId = userId;
        this.connectomeId = connectomeId;
        this.url = url;
        this.originalUrl = originalUrl;
        this.title = title;
        this.createdDate = createdDate;
    }

    public ExternalUrlTrackingDTO(String userId, String connectomeId, String url, String originalUrl, String title, String createdDate) {
        this.userId = userId;
        this.connectomeId = connectomeId;
        this.url = url;
        this.originalUrl = originalUrl;
        this.title = title;
        this.createdDate = createdDate;
    }

    public ExternalUrlTrackingDTO() {}
}
