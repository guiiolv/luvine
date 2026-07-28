package com.luvine.api.auth.dto.response;

public record AuthTokensResponse(
        String accessToken,
        String refreshToken
) {
}