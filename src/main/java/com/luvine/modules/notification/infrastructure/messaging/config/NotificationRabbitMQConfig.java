package com.luvine.modules.notification.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitMQConfig {
    public static final String EMAIL_QUEUE = "notification.email.queue";
    public static final String EMAIL_EXCHANGE = "notification.email.exchange";
    public static final String EMAIL_ROUTING_KEY = "notification.email.routing.key";
    public static final String EMAIL_DLQ_QUEUE = "notification.email.dlq.queue";
    public static final String EMAIL_DLX_EXCHANGE = "notification.email.dlx.queue";
    public static final String EMAIL_DLQ_ROUTING_KEY = "notification.emai.dlq.routingKey";

    @Bean
    public Queue emailQueue() {
        return QueueBuilder
                .durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", EMAIL_DLQ_ROUTING_KEY)
                .withArgument("x-dead-letter-routing-key", EMAIL_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Queue emailDlqQueue() {
        return new Queue(EMAIL_DLQ_QUEUE);
    }

    @Bean
    public DirectExchange emailDlxExchange() {
        return new DirectExchange(EMAIL_DLX_EXCHANGE);
    }

    @Bean
    public Binding emailDlqBinding() {
        return BindingBuilder
                .bind(emailDlqQueue())
                .to(emailDlxExchange())
                .with(EMAIL_DLQ_ROUTING_KEY);
    }
}