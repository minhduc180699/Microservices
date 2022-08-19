package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.saltlux.deepsignal.web.domain.compositekey.NotificationReceiverCompositeKey;
import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@Table(name = "notification_receiver")
@IdClass(NotificationReceiverCompositeKey.class)
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NotificationReceiver implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    @JsonBackReference
    private Notification notification;

    @Id
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
}
