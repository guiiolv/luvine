package com.luvine.modules.auth.domain.rules;

import com.luvine.common.domain.BusinessRule;

import java.time.Instant;

public class CodeMustNotBeInvalidatedRule implements BusinessRule {

    private final Instant invalidatedAt;

    public CodeMustNotBeInvalidatedRule(Instant invalidatedAt) {
        this.invalidatedAt = invalidatedAt;
    }

    @Override
    public boolean isBroken() {
        return invalidatedAt != null;
    }

    @Override
    public String message() {
        return "Esse código de verificação é inválido.";
    }
}