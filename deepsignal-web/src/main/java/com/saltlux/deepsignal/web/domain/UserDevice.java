package com.saltlux.deepsignal.web.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "user_device")
@Data
public class UserDevice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    private String deviceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private String secretKey;

    public UserDevice(String deviceId, User user, String secretKey) {
        this.deviceId = deviceId;
        this.user = user;
        this.secretKey = secretKey;
    }

    public UserDevice() {}
}
