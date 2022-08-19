package com.saltlux.deepsignal.web.domain;

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
 * A Code service type
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "code_service_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CodeServiceType implements Serializable {

    @Id
    @Column(name = "service_type", nullable = false, length = 20)
    private String serviceType;

    @Column(name = "code_order", nullable = false, columnDefinition = "TINYINT(3)")
    private Integer codeOrder;

    @Column(name = "is_disabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDisabled;

    @Column(name = "description", length = 512)
    private String description;

    @CreatedDate
    @Column(name = "registered_at", updatable = false)
    private Instant registeredAt = Instant.now();
}
