package com.luvine.modules.notification.infrastructure.messaging.message;

public record SendEmailVerificationCodeMessage(
        String recipientEmail,
        String recipientName,
        String code
) {
}