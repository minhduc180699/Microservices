package com.saltlux.deepsignal.web.config;

import com.saltlux.deepsignal.web.util.AppUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
public class RabbitmqConfiguration {

    private final String D_CLASS_IP = AppUtil.getDClassOfIP();

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    Queue queueNotification() {
        return new Queue(applicationProperties.getRabbitConfig().getNotification().getQueueName(), false);
    }

    @Bean
    Queue queueWebLogging() {
        return new Queue(applicationProperties.getRabbitConfig().getWebLogging().getQueueName(), false);
    }

    @Bean
    FanoutExchange exchangeNotification() {
        return new FanoutExchange(applicationProperties.getRabbitConfig().getNotification().getExchangeName());
    }

    @Bean
    TopicExchange exchangeWebLogging() {
        return new TopicExchange(applicationProperties.getRabbitConfig().getWebLogging().getExchangeName());
    }

    @Bean
    Binding bindingNotification(@Qualifier("queueNotification") Queue queue, @Qualifier("exchangeNotification") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    Binding bindingWebLogging(@Qualifier("queueWebLogging") Queue queue, @Qualifier("exchangeWebLogging") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(applicationProperties.getRabbitConfig().getWebLogging().getRouterKey());
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
