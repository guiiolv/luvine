package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class LastNameCannotBeEmptyRule implements BusinessRule {

    @Override
    public boolean isBroken() {
        return true;
    }

    @Override
    public String message() {
        return "O sobrenome não pode ser vazio.";
    }
}