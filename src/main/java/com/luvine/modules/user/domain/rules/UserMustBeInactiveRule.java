package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class UserMustBeInactiveRule implements BusinessRule {

    private final boolean active;

    public UserMustBeInactiveRule(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isBroken() {
        return active;
    }

    @Override
    public String message() {
        return "O usuário já está ativo.";
    }
}