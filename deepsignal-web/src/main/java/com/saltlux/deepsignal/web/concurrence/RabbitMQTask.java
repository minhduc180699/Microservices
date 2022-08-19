package com.saltlux.deepsignal.web.concurrence;

import com.saltlux.deepsignal.web.api.websocket.dto.MessageWrapper;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import javax.validation.constraints.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/*
 * Using this bean to push message into RabbitMQ
 * */
@Component
public class RabbitMQTask implements Runnable {

    @NotNull
    private String URL_MESSAGE;

    @NotNull
    private Object object;

    @NotNull
    private String exchangeName;

    @NotNull
    private String routerKey;

    // always start with /topic. To config it, see configureMessageBroker method in WebsocketConfiguration class
    public void setURL_MESSAGE(String URL_MESSAGE) {
        this.URL_MESSAGE = "/topic" + URL_MESSAGE;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setRouterKey(String routerKey) {
        this.routerKey = routerKey;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void run() {
        //        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        //        if (null != SecurityContextHolder.getContext().getAuthentication().getPrincipal()) {
        //            rabbitTemplate.setBeforePublishPostProcessors(
        //                message -> {
        //                    message
        //                        .getMessageProperties()
        //                        .setHeader("x-user-id", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        //                    return message;
        //                }
        //            );
        //        }
        rabbitTemplate.convertAndSend(exchangeName, routerKey, new MessageWrapper(URL_MESSAGE, object));
    }
}
