package com.luvine.common.application;

import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidCommandException extends RuntimeException {

    private final Map<String, String> errors;

    public InvalidCommandException(Map<String, String> errors) {
        super("A validação do comando falhou.");
        this.errors = errors;
    }
}
