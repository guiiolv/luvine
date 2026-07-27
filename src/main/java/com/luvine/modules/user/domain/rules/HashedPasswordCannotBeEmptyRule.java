package com.luvine.modules.user.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class HashedPasswordCannotBeEmptyRule implements BusinessRule {

    @Override
    public boolean isBroken() {
        return true;
    }

    @Override
    public String message() {
        return "A senha não pode ser vazia.";
    }
}