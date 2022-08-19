package com.saltlux.deepsignal.web.api.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageWrapper {

    private String urlMessage;
    private Object data;

    public MessageWrapper(Object data) {
        this.data = data;
    }
}
