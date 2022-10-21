package com.saltlux.deepsignal.web.service.dto;

import lombok.Data;

@Data
public class UrlTrackingDTO {

    private String url;

    private long clickedCount;

    public UrlTrackingDTO(long clickedCount) {
        this.clickedCount = (int) clickedCount;
    }

    public UrlTrackingDTO(String url, long clickedCount) {
        this.setUrl(url);
        this.clickedCount = clickedCount;
    }
}
