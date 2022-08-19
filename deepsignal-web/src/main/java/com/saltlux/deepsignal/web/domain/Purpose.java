package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@Entity
@Table(name = "purpose")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Purpose {

    @Id
    @GenericGenerator(
        name = "ds-sequence",
        strategy = "com.saltlux.deepsignal.web.util.StringSequenceIdentifier",
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "hibernate_sequence"),
            @org.hibernate.annotations.Parameter(name = "sequence_prefix", value = "PID_"),
        }
    )
    @GeneratedValue(generator = "ds-sequence", strategy = GenerationType.SEQUENCE)
    private String id;

    @Column(name = "purpose_name", nullable = false, length = 50)
    private String purposeName;

    @Column(name = "description", length = 512)
    private String description;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();
}
