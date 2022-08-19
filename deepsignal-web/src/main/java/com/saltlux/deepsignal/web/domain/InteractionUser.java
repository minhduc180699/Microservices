package com.saltlux.deepsignal.web.domain;

import com.saltlux.deepsignal.web.domain.compositekey.InteractionUserCompositeKey;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "interaction_user")
@Getter
@Setter
@IdClass(InteractionUserCompositeKey.class)
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InteractionUser implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "interaction_id", nullable = false)
    private Interaction interaction;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    private String feedId;

    @Id
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();

    private Integer type;
}
