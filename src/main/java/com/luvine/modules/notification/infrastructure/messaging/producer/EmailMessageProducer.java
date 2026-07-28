package com.luvine.modules.notification.infrastructure.messaging.producer;

import com.luvine.common.domain.util.EmailMaskUtil;
import com.luvine.modules.notification.infrastructure.messaging.config.NotificationRabbitMQConfig;
import com.luvine.modules.notification.infrastructure.messaging.message.SendEmailVerificationCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
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

        log.info(
                "Mensagem de envio de e-mail de verificação publicada na fila. Destinatário: {}",
                EmailMaskUtil.mask(message.recipientEmail())
        );
    }
}