package com.saltlux.deepsignal.adapter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "project")
public class Project implements Serializable {

    @Id
    @Column(name = "project_id", nullable = false, length = 40)
    private String projectId;

    @Column(name = "company_id", nullable = false, length = 40)
    private String companyId;

    @Column(name = "project_name", nullable = false, length = 256)
    private String projectName;

    @Column(name = "searvice_data_from")
    private Date searviceDataFrom;

    @Column(name = "service_data_to")
    private Date serviceDataTo;

    @Column(name = "is_closed", nullable = false, columnDefinition = "TINYINT(4)")
    private Integer isClosed = 0;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "index_name_prefix", length = 50)
    private String indexNamePrefix;

    @JsonIgnore
    private transient Instant registeredAt = Instant.now();

    @JsonIgnore
    private transient Instant lastModifiedAt = Instant.now();
}
