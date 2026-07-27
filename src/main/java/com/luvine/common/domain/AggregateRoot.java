package com.luvine.common.domain;

import com.luvine.common.domain.exception.BusinessRuleValidationException;

public abstract class AggregateRoot<T> {

    public abstract T getId();

    protected final void checkRule(BusinessRule rule) {
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}