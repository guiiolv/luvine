package com.luvine.common.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<T> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public abstract T getId();

    protected void addDomainEvents(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    protected void checkRule(BusinessRule rule) {
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}