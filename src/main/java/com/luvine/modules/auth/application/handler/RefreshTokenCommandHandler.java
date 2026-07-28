package com.luvine.modules.auth.application.handler;

import com.luvine.common.domain.exception.UnauthorizedException;
import com.luvine.modules.auth.application.command.RefreshTokenCommand;
import com.luvine.modules.auth.application.dto.AuthTokensDto;
import com.luvine.modules.auth.domain.entity.RefreshToken;
import com.luvine.modules.auth.domain.rules.TokenHash;
import com.luvine.modules.auth.infrastructure.repository.RefreshTokenRepository;
import com.luvine.modules.auth.infrastructure.service.JwtService;
import com.luvine.modules.user.domain.entity.UserCredentials;
import com.luvine.modules.user.infrastructure.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class RefreshTokenCommandHandler {

    private static final String UNAUTHORIZED_MESSAGE = "Token inválido ou expirado.";

    private final UserCredentialsRepository credentialsRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenCommandHandler(
            UserCredentialsRepository credentialsRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService) {
        this.credentialsRepository = credentialsRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthTokensDto handle(RefreshTokenCommand command) {
        String tokenHash = TokenHash.fromRawToken(command.rawToken()).value();

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(new TokenHash(tokenHash))
                .orElseThrow(() -> {
                    log.warn("Tentativa de renovação com refresh token inválido.");
                    return new UnauthorizedException(UNAUTHORIZED_MESSAGE);
                });

        if (refreshToken.isRevoked()) {
            log.warn(
                    "Tentativa de reutilização de refresh token revogado. Usuário: {}",
                    refreshToken.getUserPublicId()
            );
            refreshTokenRepository.revokeAllUserTokens(refreshToken.getUserPublicId());
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }

        if (refreshToken.isExpired()) {
            log.warn(
                    "Tentativa de utilização de refresh token expirado. Usuário: {}",
                    refreshToken.getUserPublicId()
            );
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }

        UserCredentials credentials = credentialsRepository.findByPublicId(refreshToken.getUserPublicId())
                .orElseThrow(() -> {
                    log.warn(
                            "Usuário associado ao refresh token não foi encontrado. Usuário: {}",
                            refreshToken.getUserPublicId()
                    );
                    return new UnauthorizedException(UNAUTHORIZED_MESSAGE);
                });

        refreshToken.revoke();

        String newRefreshToken = jwtService.generateRefreshToken(
                refreshToken.getUserPublicId(),
                command.deviceInfo(),
                command.ipAddress()
        );

        refreshToken.markAsReplacedBy(TokenHash.fromRawToken(newRefreshToken).value());

        refreshTokenRepository.save(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(
                credentials.getPublicId(),
                credentials.getRole()
        );

        log.info(
                "Tokens renovados com sucesso. Usuário: {}",
                credentials.getPublicId()
        );

        return new AuthTokensDto(newAccessToken, newRefreshToken);
    }
}