package com.saltlux.deepsignal.web.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saltlux.deepsignal.web.domain.CodeLanguage;
import com.saltlux.deepsignal.web.domain.CodeServiceType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

/**
 * A Web source
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class WebSourceTornadoDTO implements Serializable {

    private final String webSourceId;

    private final String webUrl;

    private String category;

    private String condition;

    private boolean isAttachment = false;

    private boolean isComment = false;

    private Instant lastModifiedAt = Instant.now();

    private Instant registeredAt = Instant.now();

    private int state = 2;

    private String serviceCrawlerType;

    private String webSourceName;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private CodeServiceType serviceType;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private CodeLanguage serviceLanguage;

    private Set<ProjectDTO> projects = new HashSet<>();

    @Data
    @AllArgsConstructor
    public static class ProjectDTO {

        private String projectId;
    }
}
