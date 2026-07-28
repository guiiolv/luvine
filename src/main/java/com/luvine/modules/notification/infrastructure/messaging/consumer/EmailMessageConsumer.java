package com.luvine.modules.notification.infrastructure.messaging.consumer;

import com.luvine.modules.notification.domain.service.EmailSenderService;
import com.luvine.modules.notification.domain.valueobject.EmailTemplate;
import com.luvine.modules.notification.infrastructure.messaging.config.NotificationRabbitMQConfig;
import com.luvine.modules.notification.infrastructure.messaging.message.SendEmailVerificationCodeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailMessageConsumer {

    private final EmailSenderService senderService;

    public EmailMessageConsumer(EmailSenderService senderService) {
        this.senderService = senderService;
    }

    @RabbitListener(
            queues = NotificationRabbitMQConfig.EMAIL_QUEUE,
            containerFactory = "simpleRabbitListenerContainerFactory"
    )
    public void publish(SendEmailVerificationCodeMessage message) {
        senderService.send(
                EmailTemplate.EMAIL_VERIFICATION_CODE,
                message.recipientEmail(),
                message.recipientName(),
                Map.of(
                        "recipientName", message.recipientName(),
                        "digit1", String.valueOf(message.code().charAt(0)),
                        "digit2", String.valueOf(message.code().charAt(1)),
                        "digit3", String.valueOf(message.code().charAt(2)),
                        "digit4", String.valueOf(message.code().charAt(3)),
                        "digit5", String.valueOf(message.code().charAt(4)),
                        "digit6", String.valueOf(message.code().charAt(5))
                )
        );
    }
}