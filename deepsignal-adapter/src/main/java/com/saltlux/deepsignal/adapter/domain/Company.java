package com.saltlux.deepsignal.adapter.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Company extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "company_id", nullable = false, length = 40)
    private String companyId;

    @Column(name = "company_name", nullable = false, length = 256)
    private String companyName;
}
