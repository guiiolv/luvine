package com.luvine.api.auth;

import com.luvine.api.auth.doc.AuthDoc;
import com.luvine.api.auth.dto.request.LoginRequest;
import com.luvine.api.auth.dto.request.RegisterRequest;
import com.luvine.api.auth.dto.request.ResendEmailVerificationRequest;
import com.luvine.api.auth.dto.request.VerifyEmailRequest;
import com.luvine.api.auth.dto.response.AuthTokensResponse;
import com.luvine.modules.auth.application.command.*;
import com.luvine.modules.auth.application.dto.AuthTokensDto;
import com.luvine.modules.auth.application.handler.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthDoc {

    private final RegisterCommandHandler registerCommandHandler;
    private final LoginCommandHandler loginCommandHandler;
    private final RefreshTokenCommandHandler refreshTokenCommandHandler;
    private final VerifyEmailCommandHandler verifyEmailCommandHandler;
    private final ResendEmailVerificationCommandHandler resendEmailVerificationCommandHandler;

    public AuthController(
            RegisterCommandHandler registerCommandHandler,
            LoginCommandHandler loginCommandHandler,
            RefreshTokenCommandHandler refreshTokenCommandHandler,
            VerifyEmailCommandHandler verifyEmailCommandHandler,
            ResendEmailVerificationCommandHandler resendEmailVerificationCommandHandler) {
        this.registerCommandHandler = registerCommandHandler;
        this.loginCommandHandler = loginCommandHandler;
        this.refreshTokenCommandHandler = refreshTokenCommandHandler;
        this.verifyEmailCommandHandler = verifyEmailCommandHandler;
        this.resendEmailVerificationCommandHandler = resendEmailVerificationCommandHandler;
    }

    @Override
    public ResponseEntity<Void> register(RegisterRequest request) {
        registerCommandHandler.handle(new RegisterCommand(
                request.email(),
                request.firstName(),
                request.lastName(),
                request.password()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<AuthTokensResponse> login(LoginRequest request, HttpServletRequest httpRequest) {
        String deviceInfo = httpRequest.getHeader("User-Agent");

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        AuthTokensDto tokensDto = loginCommandHandler.handle(new LoginCommand(
                request.email(),
                request.password(),
                deviceInfo,
                ipAddress
        ));

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokensDto.refreshToken())
                .httpOnly(true)
                .secure(true)
                .maxAge(7 * 24 * 60 * 60L)
                .path("/api/v1/auth/refresh")
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthTokensResponse(tokensDto.accessToken(), null));
    }

    @Override
    public ResponseEntity<AuthTokensResponse> refresh(String refreshToken, HttpServletRequest httpRequest) {
        String deviceInfo = httpRequest.getHeader("User-Agent");

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        AuthTokensDto tokensDto = refreshTokenCommandHandler.handle(new RefreshTokenCommand(
                refreshToken,
                deviceInfo,
                ipAddress
        ));

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokensDto.refreshToken())
                .httpOnly(true)
                .secure(true)
                .maxAge(7 * 24 * 60 * 60L)
                .path("/api/v1/auth/refresh")
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthTokensResponse(tokensDto.accessToken(), null));
    }

    @Override
    public ResponseEntity<Void> verifyEmail(VerifyEmailRequest request) {
        verifyEmailCommandHandler.handle(new VerifyEmailCommand(
                request.email(),
                request.code()
        ));

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> resendVerificationEmail(ResendEmailVerificationRequest request) {
        resendEmailVerificationCommandHandler.handle(new ResendEmailVerificationCommand(
                request.email()
        ));

        return ResponseEntity.ok().build();
    }
}