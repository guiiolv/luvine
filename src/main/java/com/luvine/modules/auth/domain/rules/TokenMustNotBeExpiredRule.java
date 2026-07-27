package com.luvine.modules.auth.domain.rules;

import com.luvine.common.domain.BusinessRule;

import java.time.Instant;

public class TokenMustNotBeExpiredRule implements BusinessRule {

    private final Instant expiresAt;

    public TokenMustNotBeExpiredRule(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean isBroken() {
        return Instant.now().isAfter(expiresAt);
    }

    @Override
    public String message() {
        return "Esse token já expirou.";
    }
}