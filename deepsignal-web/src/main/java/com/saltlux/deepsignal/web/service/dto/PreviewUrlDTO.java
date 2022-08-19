package com.saltlux.deepsignal.web.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreviewUrlDTO {

    private String domain;

    private String url;

    private String title;

    private String desc;

    private String image;

    private String imageAlt;
}
