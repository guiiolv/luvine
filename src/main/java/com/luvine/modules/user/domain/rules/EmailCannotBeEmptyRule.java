package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class EmailCannotBeEmptyRule implements BusinessRule {

    @Override
    public boolean isBroken() {
        return true;
    }

    @Override
    public String message() {
        return "O e-mail não pode ser vazio.";
    }
}