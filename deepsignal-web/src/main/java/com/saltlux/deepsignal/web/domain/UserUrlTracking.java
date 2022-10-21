package com.saltlux.deepsignal.web.domain;

import com.saltlux.deepsignal.web.domain.compositekey.UserUrlTrackingId;
import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "user_url_tracking")
@IdClass(UserUrlTrackingId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserUrlTracking implements Serializable {

    @Id
    private String userId;

    @Id
    private Long externalUrlId;

    @Column(name = "click")
    private int click;
}
