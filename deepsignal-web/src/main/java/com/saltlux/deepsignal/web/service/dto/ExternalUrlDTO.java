package com.saltlux.deepsignal.web.service.dto;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ExternalUrlDTO implements Serializable {

    private Long id;

    private String url;

    private String originalUrl;

    private String shortUrl;

    private String title;

    private Instant createdDate = Instant.now();

    public ExternalUrlDTO(Long id, String url, String originalUrl, String shortUrl, String title, Instant createdDate) {
        this.id = id;
        this.url = url;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.title = title;
        this.createdDate = createdDate;
    }

    public ExternalUrlDTO(String url, String originalUrl, String shortUrl, String title, Instant createdDate) {
        this.url = url;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.title = title;
        this.createdDate = createdDate;
    }

    public ExternalUrlDTO() {}
}
