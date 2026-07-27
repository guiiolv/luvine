package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class EmailMustBeValidRule implements BusinessRule {

    private final String value;

    public EmailMustBeValidRule(String value) {
        this.value = value;
    }

    @Override
    public boolean isBroken() {
        return !value.matches("^[a-zA-Z0-9._%+]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    @Override
    public String message() {
        return "O e-mail é inválido.";
    }
}