package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class EmailMustNotBeVerifiedRule implements BusinessRule {

    private final boolean emailVerified;

    public EmailMustNotBeVerifiedRule(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public boolean isBroken() {
        return emailVerified;
    }

    @Override
    public String message() {
        return "O e-mail já foi verificado.";
    }
}