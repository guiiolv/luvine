package com.luvine.modules.auth.domain.rules;

import com.luvine.common.domain.BusinessRule;

import java.time.Instant;

public class CodeMustNotBeVerifiedRule implements BusinessRule {

    private final Instant verifiedAt;

    public CodeMustNotBeVerifiedRule(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    @Override
    public boolean isBroken() {
        return verifiedAt != null;
    }

    @Override
    public String message() {
        return "Esse código de verificação já foi usado.";
    }
}