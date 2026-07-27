package com.luvine.modules.auth.application.command;

public record LoginCommand(
        String email,
        String password,
        String deviceInfo,
        String ipAddress
) {
}