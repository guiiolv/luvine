package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;
import com.luvine.modules.user.domain.valueobject.LastName;

public class LastNameMustBeDifferentRule implements BusinessRule {

    private final LastName current;
    private final LastName newLastName;

    public LastNameMustBeDifferentRule(LastName current, LastName newLastName) {
        this.current = current;
        this.newLastName = newLastName;
    }

    @Override
    public boolean isBroken() {
        return current.equals(newLastName);
    }

    @Override
    public String message() {
        return "O usuário já possui o sobrenome informado.";
    }
}