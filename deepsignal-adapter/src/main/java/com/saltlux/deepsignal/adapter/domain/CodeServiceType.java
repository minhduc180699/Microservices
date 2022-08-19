package com.saltlux.deepsignal.adapter.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "code_service_type")
public class CodeServiceType extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "service_type", nullable = false, length = 20)
    private String serviceType;

    @Column(name = "code_order", nullable = false, columnDefinition = "TINYINT(2)")
    private Integer codeOrder;

    @Column(name = "is_disabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDisabled;

    @Column(name = "description", length = 512)
    private String description;
}
