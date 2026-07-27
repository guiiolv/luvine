package com.luvine.modules.user.domain.valueobject;

import com.luvine.common.domain.BusinessRule;
import com.luvine.common.domain.exception.BusinessRuleValidationException;
import com.luvine.common.domain.util.NameUtil;
import com.luvine.modules.user.domain.rules.LastNameCannotBeEmptyRule;
import com.luvine.modules.user.domain.rules.LastNameMustBeValidRule;
import jakarta.persistence.Embeddable;

@Embeddable
public record LastName(String value) {
    public LastName {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleValidationException(new LastNameCannotBeEmptyRule());
        }

        value = NameUtil.normalize(value);

        BusinessRule rule = new LastNameMustBeValidRule(value);
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}