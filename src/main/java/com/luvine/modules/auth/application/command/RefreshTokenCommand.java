package com.luvine.modules.auth.application.command;

public record RefreshTokenCommand(
        String rawToken,
        String deviceInfo,
        String ipAddress
) {
}