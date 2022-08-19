package com.saltlux.deepsignal.web.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Web source template
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "web_source_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WebSourceTemplate implements Serializable {

    @Id
    @Column(name = "template_id", nullable = false, length = 32)
    private String templateId;

    @Column(name = "site_name", nullable = false, length = 50)
    private String siteName;

    @Column(name = "section_name", nullable = false, length = 50)
    private String sectionName;

    @Column(name = "web_url", nullable = false, length = 256)
    private String webUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type", nullable = false)
    private CodeServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_language", nullable = false)
    private CodeLanguage serviceLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_crawler_type", nullable = false)
    private CodeServiceCrawlerType serviceCrawlerType;
}
