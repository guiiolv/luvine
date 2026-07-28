package com.luvine.modules.auth.application.command;

public record RequestEmailVerificationCommand(
        String email
) {
}