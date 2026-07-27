package com.luvine.common.domain;

public interface BusinessRule {

    boolean isBroken();

    String message();
}