package com.luvine.api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(
        String email,

        @NotBlank(message = "Informe o código de verificação")
        String code
) {
}