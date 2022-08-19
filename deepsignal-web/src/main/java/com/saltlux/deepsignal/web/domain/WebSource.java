package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * A Web source
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "web_source")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WebSource implements Serializable {

    @Id
    @GenericGenerator(
        name = "ds-sequence",
        strategy = "com.saltlux.deepsignal.web.util.StringSequenceIdentifier",
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "hibernate_sequence"),
            @org.hibernate.annotations.Parameter(name = "sequence_prefix", value = "WID_"),
        }
    )
    @GeneratedValue(generator = "ds-sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "web_source_id", nullable = false, length = 40)
    private String webSourceId;

    @Column(name = "web_url", nullable = false, unique = true, length = 256)
    private String webUrl;

    @Column(name = "category", length = 256)
    private String category;

    @Column(name = "\"condition\"", length = 512)
    private String condition;

    @Column(name = "is_attachment", nullable = false)
    private boolean isAttachment = false;

    @Column(name = "is_comment", nullable = false)
    private boolean isComment = false;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    @JsonIgnore
    private Instant lastModifiedAt = Instant.now();

    @CreatedDate
    @Column(name = "registered_at", updatable = false)
    private Instant registeredAt = Instant.now();

    @Column(name = "state")
    private int state = 0;

    @Column(name = "service_crawler_type", length = 20)
    private String serviceCrawlerType;

    @Column(name = "web_source_name", length = 50)
    private String webSourceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private CodeServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @JoinColumn(name = "service_language", nullable = false)
    private CodeLanguage serviceLanguage;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "connectome_web_source_detail",
        joinColumns = { @JoinColumn(name = "web_source_id", referencedColumnName = "web_source_id") },
        inverseJoinColumns = { @JoinColumn(name = "connectome_id", referencedColumnName = "connectome_id") }
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Connectome> connectomes = new HashSet<>();
}
