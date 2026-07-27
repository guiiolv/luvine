package com.luvine.api.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        String message,
        String path,
        Map<String, String> errors,
        Instant timestamp
) {
    public static ApiError of(String message, String path) {
        return new ApiError(message, path, null, Instant.now());
    }

    public static ApiError ofValidation(String message, String path, Map<String, String> errors) {
        return new ApiError(message, path, errors, Instant.now());
    }
}