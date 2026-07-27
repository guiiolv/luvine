package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;
import com.luvine.modules.user.domain.valueobject.Role;

public class RoleMustBeDifferentRule implements BusinessRule {

    private final Role current;
    private final Role newRole;

    public RoleMustBeDifferentRule(Role current, Role newRole) {
        this.current = current;
        this.newRole = newRole;
    }

    @Override
    public boolean isBroken() {
        return current == newRole;
    }

    @Override
    public String message() {
        return "O usuário já possui a permissão informada.";
    }
}