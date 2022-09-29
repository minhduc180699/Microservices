package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saltlux.deepsignal.web.config.Constants;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * A user.
 */
@Getter
@Setter
@Entity
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NoArgsConstructor
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
        name = "ds-sequence",
        strategy = "com.saltlux.deepsignal.web.util.StringSequenceIdentifier",
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "hibernate_sequence"),
            @org.hibernate.annotations.Parameter(name = "sequence_prefix", value = "UID_"),
        }
    )
    @GeneratedValue(generator = "ds-sequence", strategy = GenerationType.SEQUENCE)
    private String id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Integer activated;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_purpose_detail",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "purpose_id", referencedColumnName = "id") }
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Purpose> purposes = new HashSet<>();

    @Column(name = "phone_number")
    //    @NotNull
    //    @Pattern(regexp = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message = "Not correct format phone number")
    private String phoneNumber;

    @Column(name = "phone_country")
    //    @NotNull
    private String phoneCountry;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer termOfService;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer receiveNews;

    @Column(name = "login_failed_count", columnDefinition = "TINYINT(1)")
    private Integer loginFailedCount;

    @Column(name = "locked_on")
    private Instant lockedOn;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "ip")
    private String ip;

    @NotNull
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Integer deleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Connectome> connectomes;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "user-interaction")
    private Set<InteractionUser> interactionUsers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "user-comment")
    private Set<Comment> comments = new HashSet<>();

    @Column(name = "time_zone")
    private String timeZone;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserDevice> userDevices;

    public User(String id) {
        this.id = id;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
