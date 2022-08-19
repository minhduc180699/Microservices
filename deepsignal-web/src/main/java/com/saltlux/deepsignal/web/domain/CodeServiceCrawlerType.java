package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

/**
 * A Code service crawler type
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "code_service_crawler_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CodeServiceCrawlerType implements Serializable {

    @Id
    @Column(name = "service_crawler_type", nullable = false, length = 50)
    private String serviceCrawlerType;

    @Column(name = "is_disabled", nullable = false, columnDefinition = "TINYINT(4)")
    private Integer isDisabled;

    @Column(name = "description", length = 512)
    private String description;

    @CreatedDate
    @Column(name = "registered_at", updatable = false)
    @JsonIgnore
    private Instant registeredAt = Instant.now();

    @Column(name = "channel_code")
    private Integer channelCode;
}
