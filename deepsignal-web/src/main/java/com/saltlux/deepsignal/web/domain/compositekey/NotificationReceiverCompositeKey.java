package com.saltlux.deepsignal.web.domain.compositekey;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class NotificationReceiverCompositeKey implements Serializable {

    private Long notification;
    private String receiver;
}
