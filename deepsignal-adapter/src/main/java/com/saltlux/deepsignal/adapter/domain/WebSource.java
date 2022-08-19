package com.saltlux.deepsignal.adapter.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "web_source")
public class WebSource extends AbstractAuditingEntity implements Serializable {

    @Id
    @GenericGenerator(
        name = "ds-sequence",
        strategy = "com.saltlux.deepsignal.adapter.util.StringSequenceIdentifier",
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "hibernate_sequence"),
            @org.hibernate.annotations.Parameter(name = "sequence_prefix", value = "WID_"),
        }
    )
    @GeneratedValue(generator = "ds-sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "web_source_id", nullable = false, length = 50)
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

    @Column(name = "state")
    private Integer state = 0;

    @Column(name = "service_crawler_type", length = 20)
    private String serviceCrawlerType;

    @Column(name = "web_source_name", length = 50)
    private String webSourceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type", nullable = false)
    private CodeServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_language", nullable = false)
    private CodeLanguage serviceLanguage;

    @ManyToMany
    @JoinTable(
        name = "project_web_source",
        joinColumns = { @JoinColumn(name = "web_source_id", referencedColumnName = "web_source_id") },
        inverseJoinColumns = { @JoinColumn(name = "project_id", referencedColumnName = "project_id") }
    )
    private Set<Project> projects = new HashSet<>();
}
