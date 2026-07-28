package com.luvine.api.auth.dto.request;

import com.luvine.api.auth.validator.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordMatch
public record RegisterRequest(
        @NotBlank(message = "Informe o seu e-mail.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Esse e-mail é inválido.")
        String email,

        @NotBlank(message = "Informe o seu nome.")
        @Size(min = 2, max = 100, message = "O nome deve conter entre 2 a 100 caracteres.")
        @Pattern(regexp = "^[\\p{L} ]+$", message = "O nome não deve conter números ou caracteres especiais.")
        String firstName,

        @NotBlank(message = "Informe o seu sobrenome.")
        @Size(min = 2, max = 100, message = "O sobrenome deve conter entre 2 a 100 caracteres.")
        @Pattern(regexp = "^[\\p{L} ]+$", message = "O sobrenome não deve conter números ou caracteres especiais.")
        String lastName,

        @NotBlank(message = "Informe a sua senha.")
        @Size(min = 8, message = "A senha deve conter ao menos 8 caracteres.")
        @Pattern(regexp = "^(?=[^A-Z]*+[A-Z])(?=[^a-z]*+[a-z])(?=\\D*+\\d)(?=[^#?!@$%^&*-]*+[#?!@$%^&*-]).{8,64}$",
                message = "Essa senha é inválida.")
        String password,

        @NotBlank(message = "É necessário confirmar sua senha.")
        String confirmPassword
) {
}