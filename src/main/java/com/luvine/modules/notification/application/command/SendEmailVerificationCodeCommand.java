package com.luvine.modules.notification.application.command;

public record SendEmailVerificationCodeCommand(
        String recipientEmail,
        String recipientName,
        String code
) {
}