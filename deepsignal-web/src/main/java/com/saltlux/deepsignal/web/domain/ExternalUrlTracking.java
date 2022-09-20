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
@Table(name = "external_url_tracking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExternalUrlTracking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connectome_id")
    private Connectome connectome;

    @Column(name = "url")
    private String url;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    public ExternalUrlTracking() {}
}
