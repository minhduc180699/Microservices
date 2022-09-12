package com.saltlux.deepsignal.web.service.dto;

import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingDocumentInfoDTO implements Serializable {

    private String author;

    private String name;

    private String description;

    private String keyword;

    private String type;

    private String url;

    private Instant datetime;

    public TrainingDocumentInfoDTO() {}

    public TrainingDocumentInfoDTO(
        String author,
        String name,
        String description,
        String keyword,
        String type,
        String url,
        Instant datetime
    ) {
        this.author = author;
        this.name = name;
        this.description = description;
        this.keyword = keyword;
        this.type = type;
        this.url = url;
        this.datetime = datetime;
    }
}
