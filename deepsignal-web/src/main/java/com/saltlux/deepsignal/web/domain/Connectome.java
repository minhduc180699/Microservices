package com.saltlux.deepsignal.web.domain;

import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * A Customer SNS account
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "connectome")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Connectome {

    @Id
    @GenericGenerator(
        name = "ds-sequence",
        strategy = "com.saltlux.deepsignal.web.util.StringSequenceIdentifier",
        parameters = {
            @Parameter(name = "sequence_name", value = "hibernate_sequence"), @Parameter(name = "sequence_prefix", value = "CID_"),
        }
    )
    @GeneratedValue(generator = "ds-sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "connectome_id", columnDefinition = "CHAR(40)", updatable = false, nullable = false)
    private String connectomeId;

    @Column(name = "connectome_name", nullable = false, length = 50)
    private String connectomeName;

    @Column(name = "connectome_job", nullable = false, length = 50)
    private String connectomeJob;

    @Column(name = "description", length = 512)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @OneToMany(mappedBy = "connectome", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LinkShare> linkShares;

    public Connectome(String connectomeId) {
        this.connectomeId = connectomeId;
    }
}
