package com.saltlux.deepsignal.web.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A social network type.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "social_network")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SocialNetwork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11)", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Size(max = 25)
    @Column(name = "social_network_name", length = 25)
    private String socialNetworkName;
}
