package com.luvine.modules.auth.application.command;

public record EmailVerifyCommand(
        String email,
        String code
) {
}