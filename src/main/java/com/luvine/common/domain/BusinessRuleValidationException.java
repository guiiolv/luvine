package com.luvine.common.domain;

import lombok.Getter;

@Getter
public class BusinessRuleValidationException extends RuntimeException {

    private final BusinessRule brokenRule;

    public BusinessRuleValidationException(BusinessRule brokenRule) {
        super(brokenRule.message());
        this.brokenRule = brokenRule;
    }
}