package com.luvine.modules.user.domain.valueobject;

import com.luvine.common.domain.BusinessRule;
import com.luvine.common.domain.exception.BusinessRuleValidationException;
import com.luvine.common.domain.util.NameUtil;
import com.luvine.modules.user.domain.rules.FirstNameCannotBeEmptyRule;
import com.luvine.modules.user.domain.rules.FirstNameMustBeValidRule;
import jakarta.persistence.Embeddable;

@Embeddable
public record FirstName(String value) {
    public FirstName {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleValidationException(new FirstNameCannotBeEmptyRule());
        }

        value = NameUtil.normalize(value);

        BusinessRule rule = new FirstNameMustBeValidRule(value);
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}