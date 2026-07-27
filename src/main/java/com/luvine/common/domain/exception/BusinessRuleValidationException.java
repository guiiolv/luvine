package com.luvine.common.domain.exception;

import com.luvine.common.domain.BusinessRule;
import lombok.Getter;

@Getter
public class BusinessRuleValidationException extends DomainException {

    private final BusinessRule rule;

    public BusinessRuleValidationException(BusinessRule rule) {
        super(rule.message());
        this.rule = rule;
    }
}