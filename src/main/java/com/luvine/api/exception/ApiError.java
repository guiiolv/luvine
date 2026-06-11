package com.luvine.api.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class ApiError {
    private final Instant timestamp;
    private final Integer status;
    private final String error;
    private final String message;
    private final List<String> details;
    private final String path;

    private ApiError(Integer status, String error, String message, List<String> details, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details == null ? List.of() : List.copyOf(details);
        this.path = path;
    }

    public static ApiError create(Integer status, String error, String message, List<String> details, String path) {
        return new ApiError(status, error, message, details, path);
    }
}