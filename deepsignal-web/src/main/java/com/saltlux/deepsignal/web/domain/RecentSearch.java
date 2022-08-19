package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "recent_search")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecentSearch extends AbstractBaseEntity {

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private transient String userId;

    @PostLoad
    public void onload() {
        if (Objects.nonNull(this.user)) {
            this.userId = this.user.getId();
        }
    }
}
