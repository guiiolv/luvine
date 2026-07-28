package com.luvine.modules.auth.domain.rules;

import com.luvine.common.domain.BusinessRule;

import java.time.Instant;

public class CodeMustNotBeExpiredRule implements BusinessRule {

    private final Instant expiresAt;

    public CodeMustNotBeExpiredRule(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean isBroken() {
        return Instant.now().isAfter(expiresAt);
    }

    @Override
    public String message() {
        return "Esse código de verificação já expirou.";
    }
}