package com.luvine.common.domain.exception;

public class InvalidEmailVerificationException extends DomainException {

    public InvalidEmailVerificationException(String message) {
        super(message);
    }
}