package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * A Customer SNS account
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "connectome_social_media")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConnectomeSocialMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 50)
    @Column(name = "login_username", length = 50)
    private String loginUsername;

    @JsonIgnore
    @Size(max = 60)
    @Column(name = "login_password", length = 60)
    private String loginPassword;

    @Size(max = 512)
    @Column(name = "user_token", length = 512)
    private String userToken;

    @Column(name = "login_cookies")
    private String loginCookies;

    @CreatedDate
    @Column(name = "registered_at", updatable = false)
    private Instant registeredAt = Instant.now();

    @LastModifiedDate
    @Column(name = "last_updated_at")
    @JsonIgnore
    private Instant lastModifiedAt = Instant.now();

    @Column(nullable = false)
    private int status;

    @Column(name = "social_account_state")
    private int socialAccountState;

    @Column(name = "user_token_state")
    private int userTokenState = 1;

    @Size(max = 150)
    @Column(name = "social_account_state_description", length = 150)
    private String socialAccountStateDescription;

    @Size(max = 150)
    @Column(name = "user_token_state_description", length = 150)
    private String userTokenStateDescription;

    @Size(max = 100)
    @Column(name = "uuid", length = 100)
    private String uuid;

    @Column(name = "cookies_state")
    private int cookiesState = 1;

    @Size(max = 150)
    @Column(name = "cookies_state_description", length = 150)
    private String cookiesStateDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "social_type")
    private SocialNetwork socialNetwork;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "connectome_social_media_detail",
        joinColumns = { @JoinColumn(name = "social_media_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "connectome_id", referencedColumnName = "connectome_id") }
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Connectome> connectomes = new HashSet<>();
}
