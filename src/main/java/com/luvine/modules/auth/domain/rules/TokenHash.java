package com.luvine.modules.auth.domain.rules;

import jakarta.persistence.Embeddable;

import java.security.SecureRandom;
import java.util.Base64;

@Embeddable
public record TokenHash(String value) {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static TokenHash fromRawToken(String rawToken) {
        return new TokenHash(rawToken);
    }

    public static String generateRawToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}