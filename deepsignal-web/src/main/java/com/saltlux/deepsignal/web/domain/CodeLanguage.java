package com.saltlux.deepsignal.web.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Code language
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "code_language")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CodeLanguage implements Serializable {

    @Id
    @Column(name = "language", nullable = false, length = 20)
    private String language;

    @Column(name = "description", length = 512)
    private String description;
}
