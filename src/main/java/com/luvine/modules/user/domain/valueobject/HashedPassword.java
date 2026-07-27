package com.luvine.modules.user.domain.valueobject;

import com.luvine.common.domain.exception.BusinessRuleValidationException;
import com.luvine.modules.user.domain.rules.HashedPasswordCannotBeEmptyRule;
import jakarta.persistence.Embeddable;

@Embeddable
public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleValidationException(new HashedPasswordCannotBeEmptyRule());
        }
    }
}