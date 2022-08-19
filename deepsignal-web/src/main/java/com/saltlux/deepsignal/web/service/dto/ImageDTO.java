package com.saltlux.deepsignal.web.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {

    private int width;
    private int height;
    private String url;

    public ImageDTO(String url) {
        this.url = url;
    }
}
