package com.saltlux.deepsignal.web.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "external_url")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExternalUrl implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    public ExternalUrl() {}
}
