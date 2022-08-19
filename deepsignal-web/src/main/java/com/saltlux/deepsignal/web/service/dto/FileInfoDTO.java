package com.saltlux.deepsignal.web.service.dto;

import java.time.Instant;
import java.util.Date;
import lombok.Data;

/**
 * A File upload
 */
@Data
public class FileInfoDTO {

    private Long id;

    private String name;

    private long size;

    private String path;

    private String mineType;

    private String downloadUrl;

    private String description;

    private String type;

    private String originDate;

    private Instant publishedDate;

    private Instant createdDate;

    private Instant lastModifiedDate = Instant.now();

    private String connectomeId;

    private String author;

    //    private String date;

    private String fileType;

    private String searchType;

    private String favicon;

    private String img;

    private String lang;

    private String keyword;
}
