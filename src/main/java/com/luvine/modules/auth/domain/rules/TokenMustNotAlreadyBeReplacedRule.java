package com.luvine.modules.auth.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class TokenMustNotAlreadyBeReplacedRule implements BusinessRule {

    private final String replacedByTokenHash;

    public TokenMustNotAlreadyBeReplacedRule(String replacedByTokenHash) {
        this.replacedByTokenHash = replacedByTokenHash;
    }

    @Override
    public boolean isBroken() {
        return replacedByTokenHash != null;
    }

    @Override
    public String message() {
        return "Esse token já foi substituido.";
    }
}