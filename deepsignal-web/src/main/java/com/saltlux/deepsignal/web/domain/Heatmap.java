package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
@Table(name = "heatmap")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Heatmap implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x", nullable = false)
    private int x;

    @Column(name = "y", nullable = false)
    private int y;

    @Column(name = "value", nullable = false)
    private int value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    //    private transient String userId;
    //
    //    @PostLoad
    //    public void onload() {
    //        if (Objects.nonNull(this.user)) {
    //            this.userId = this.user.getId();
    //        }
    //    }
}
