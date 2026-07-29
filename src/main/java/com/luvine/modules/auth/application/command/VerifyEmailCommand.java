package com.luvine.modules.auth.application.command;

public record VerifyEmailCommand(
        String email,
        String code
) {
}