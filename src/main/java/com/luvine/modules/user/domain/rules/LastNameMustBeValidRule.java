package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class LastNameMustBeValidRule implements BusinessRule {

    private final String value;

    public LastNameMustBeValidRule(String value) {
        this.value = value;
    }

    @Override
    public boolean isBroken() {
        return value.length() < 2 || value.length() > 100 || !value.matches("^[\\p{L} ]+$");
    }

    @Override
    public String message() {
        return "O sobrenome é inválido.";
    }
}