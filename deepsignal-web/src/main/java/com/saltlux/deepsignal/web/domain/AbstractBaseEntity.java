package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base abstract class for entities which will hold definitions for created, last modified
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    //    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();

    @PreUpdate
    void preUpdate() {
        this.lastModifiedDate = Instant.now();
    }

    public static final transient Sort SORT_BY_ID_DESC = Sort.by(Sort.Direction.DESC, "id");
    public static final transient Sort SORT_BY_CREATED_DATE_DESC = Sort.by(Sort.Direction.DESC, "createdDate");
    public static final transient Sort SORT_BY_LAST_MODIFY_DATE_DESC = Sort.by(Sort.Direction.DESC, "lastModifiedDate");
}
