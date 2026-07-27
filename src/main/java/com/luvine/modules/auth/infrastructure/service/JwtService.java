package com.luvine.modules.auth.infrastructure.service;

import com.luvine.modules.user.domain.valueobject.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
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
}