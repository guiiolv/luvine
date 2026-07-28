package com.luvine.api.auth.doc;

import com.luvine.api.auth.dto.request.LoginRequest;
import com.luvine.api.auth.dto.request.RegisterRequest;
import com.luvine.api.auth.dto.response.AuthTokensResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Autenticação e gerenciamento de tokens")
public interface AuthDoc {

    @Operation(summary = "Registrar novo usuário")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email já cadastrado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    @PostMapping("/register")
    ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request);

    @Operation(summary = "Realizar login")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = AuthTokensResponse.class)))
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    @PostMapping("/login")
    ResponseEntity<AuthTokensResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest);

    @Operation(summary = "Renovar access token")
    @ApiResponse(responseCode = "200", description = "Token renovado com sucesso",
            content = @Content(schema = @Schema(implementation = AuthTokensResponse.class)))
    @ApiResponse(responseCode = "401", description = "Refresh token inválido ou expirado", content = @Content)
    @PostMapping("/refresh")
    ResponseEntity<AuthTokensResponse> refresh(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletRequest httpRequest);
}