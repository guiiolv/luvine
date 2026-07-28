package com.luvine.modules.auth.application.dto;

public record AuthTokensDto(
        String accessToken,
        String refreshToken
) {
}