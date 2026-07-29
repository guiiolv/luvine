package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

import java.time.Instant;

public class VerificationEmailRequestMustRespectRateLimitRule implements BusinessRule {

    private final Instant now;
    private final Instant nextAllowedRequest;

    public VerificationEmailRequestMustRespectRateLimitRule(Instant now, Instant nextAllowedRequest) {
        this.now = now;
        this.nextAllowedRequest = nextAllowedRequest;
    }

    @Override
    public boolean isBroken() {
        return nextAllowedRequest != null && now.isBefore(nextAllowedRequest);
    }

    @Override
    public String message() {
        return "Aguarde alguns instantes antes de solicitar um novo código de verificação.";
    }
}