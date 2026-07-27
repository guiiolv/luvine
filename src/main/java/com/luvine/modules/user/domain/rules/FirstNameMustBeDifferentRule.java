package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;
import com.luvine.modules.user.domain.valueobject.FirstName;

public class FirstNameMustBeDifferentRule implements BusinessRule {

    private final FirstName current;
    private final FirstName newFirstName;

    public FirstNameMustBeDifferentRule(FirstName current, FirstName newFirstName) {
        this.current = current;
        this.newFirstName = newFirstName;
    }

    @Override
    public boolean isBroken() {
        return current.equals(newFirstName);
    }

    @Override
    public String message() {
        return "O usuário já possui o nome informado.";
    }
}