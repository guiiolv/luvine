package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class UserMustBeActiveRule implements BusinessRule {

    private final boolean active;

    public UserMustBeActiveRule(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isBroken() {
        return !active;
    }

    @Override
    public String message() {
        return "O usuário já está desativado.";
    }
}