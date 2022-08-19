package com.saltlux.deepsignal.web.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "link_share")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LinkShare implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "card_id")
    @NotNull
    private String cardId;

    @Column(name = "state")
    @NotNull
    private String state;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Instant createdDate = Instant.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "connectome_id", nullable = false)
    private Connectome connectome;
}
