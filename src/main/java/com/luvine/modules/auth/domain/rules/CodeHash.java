package com.luvine.modules.auth.domain.rules;

import com.luvine.modules.auth.domain.util.HashUtil;
import jakarta.persistence.Embeddable;

import java.security.SecureRandom;

@Embeddable
public record CodeHash(String value) {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static CodeHash fromRawCode(String rawCode) {
        return new CodeHash(HashUtil.hash(rawCode));
    }

    public static String generateRawCode() {
        int code = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(code);
    }
}