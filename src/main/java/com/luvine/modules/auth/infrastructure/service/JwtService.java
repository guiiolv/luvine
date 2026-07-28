package com.luvine.modules.auth.infrastructure.service;

import com.luvine.common.domain.util.RequestInfoUtil;
import com.luvine.modules.auth.domain.entity.RefreshToken;
import com.luvine.modules.auth.domain.rules.TokenHash;
import com.luvine.modules.auth.infrastructure.repository.RefreshTokenRepository;
import com.luvine.modules.user.domain.valueobject.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtService(JwtEncoder jwtEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateAccessToken(UUID userPublicId, Role role) {
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("luvine-api")
                .subject(userPublicId.toString())
                .claim("jti", UUID.randomUUID().toString())
                .claim("type", "access")
                .claim("role", role)
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.MINUTES))
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(
                JwsHeader.with(() -> "RS256").build(), jwtClaimsSet
        );

        String accessToken = jwtEncoder.encode(parameters).getTokenValue();

        log.info(
                "Token de acesso gerado com sucesso. Usuário: {}, Perfil: {}",
                userPublicId,
                role
        );

        return accessToken;
    }

    @Transactional
    public String generateRefreshToken(UUID userPublicId, String deviceInfo, String ipAddress) {
        String rawToken = TokenHash.generateRawToken();

        RefreshToken refreshToken = RefreshToken.create(
                userPublicId,
                TokenHash.fromRawToken(rawToken),
                RequestInfoUtil.truncateDeviceInfo(deviceInfo),
                RequestInfoUtil.normalizeIp(ipAddress)
        );

        refreshTokenRepository.save(refreshToken);

        log.info(
                "Refresh token emitido com sucesso. Usuário: {}",
                userPublicId
        );

        return rawToken;
    }
}