package com.saltlux.deepsignal.adapter.service.dto;

import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A File upload
 */
@Data
@NoArgsConstructor
public class FileInfoDTO implements Serializable {

    private Long id;

    private String name;

    private long size;

    private String path;

    private String mineType;

    private String downloadUrl;

    private String description;

    private String type;

    private Instant createdDate = Instant.now();

    private Instant lastModifiedDate = Instant.now();

    private String connectomeId;

    private String author;

    private String date;

    private String searchType;
}
