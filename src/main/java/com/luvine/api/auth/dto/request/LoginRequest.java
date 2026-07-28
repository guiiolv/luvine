package com.luvine.api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Informe o seu e-mail.")
        String email,

        @NotBlank(message = "Informe a sua senha.")
        String password
) {
}