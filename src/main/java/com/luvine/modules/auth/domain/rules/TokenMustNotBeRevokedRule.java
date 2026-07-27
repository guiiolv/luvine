package com.luvine.modules.auth.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class TokenMustNotBeRevokedRule implements BusinessRule {

    private final boolean revoked;

    public TokenMustNotBeRevokedRule(boolean revoked) {
        this.revoked = revoked;
    }

    @Override
    public boolean isBroken() {
        return revoked;
    }

    @Override
    public String message() {
        return "Esse token já foi revogado.";
    }
}