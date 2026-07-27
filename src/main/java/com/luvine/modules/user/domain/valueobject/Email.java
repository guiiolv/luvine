package com.luvine.modules.user.domain.valueobject;

import com.luvine.common.domain.BusinessRule;
import com.luvine.common.domain.exception.BusinessRuleValidationException;
import com.luvine.modules.user.domain.rules.EmailCannotBeEmptyRule;
import com.luvine.modules.user.domain.rules.EmailMustBeValidRule;
import jakarta.persistence.Embeddable;

@Embeddable
public record Email(String value) {
    public Email {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleValidationException(new EmailCannotBeEmptyRule());
        }

        value = normalize(value);

        BusinessRule rule = new EmailMustBeValidRule(value);
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase();
    }
}