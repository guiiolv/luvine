package com.luvine.common.domain;

public class BusinessRuleValidationException extends RuntimeException {
    public BusinessRuleValidationException(BusinessRule rule) {
        super(rule.message());
    }
}