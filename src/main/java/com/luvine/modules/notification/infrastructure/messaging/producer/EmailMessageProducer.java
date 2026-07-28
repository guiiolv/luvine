package com.luvine.modules.notification.infrastructure.messaging.producer;

import com.luvine.modules.notification.infrastructure.messaging.config.NotificationRabbitMQConfig;
import com.luvine.modules.notification.infrastructure.messaging.message.SendEmailVerificationCodeMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public EmailMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(SendEmailVerificationCodeMessage message) {
        rabbitTemplate.convertAndSend(
                NotificationRabbitMQConfig.EMAIL_EXCHANGE,
                NotificationRabbitMQConfig.EMAIL_ROUTING_KEY,
                message
        );
    }
}