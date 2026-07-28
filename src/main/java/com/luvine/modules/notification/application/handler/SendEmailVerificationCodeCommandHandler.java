package com.luvine.modules.notification.application.handler;

import com.luvine.modules.notification.application.command.SendEmailVerificationCodeCommand;
import com.luvine.modules.notification.infrastructure.messaging.message.SendEmailVerificationCodeMessage;
import com.luvine.modules.notification.infrastructure.messaging.producer.EmailMessageProducer;
import org.springframework.stereotype.Component;

@Component
public class SendEmailVerificationCodeCommandHandler {

    private final EmailMessageProducer messageProducer;

    public SendEmailVerificationCodeCommandHandler(EmailMessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    public void handle(SendEmailVerificationCodeCommand command) {
        messageProducer.publish(new SendEmailVerificationCodeMessage(
                command.recipientEmail(),
                command.recipientName(),
                command.code()
        ));
    }
}