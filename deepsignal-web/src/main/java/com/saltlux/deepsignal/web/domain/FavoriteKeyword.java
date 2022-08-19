package com.saltlux.deepsignal.web.domain;

import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "favorite_keyword")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FavoriteKeyword extends AbstractBaseEntity {

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private transient String userId;

    @Column(name = "connectome_id", nullable = false, length = 40)
    private String connectomeId;

    @PostLoad
    public void onload() {
        if (Objects.nonNull(this.user)) {
            this.userId = this.user.getId();
        }
    }
}
