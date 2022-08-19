package com.saltlux.deepsignal.web.service.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlUploadDTO {

    private String name;
    private String url;
    private String author;
    private String originDate;
    private String publishedDate;
    private String searchType;
    private String favicon;
    private String img;
    private String description;
    private String keyword;
    private String type;
}
