package com.luvine.modules.auth.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "app.jwt")
public record TokenProperties(
        RSAPrivateKey privateKey,
        RSAPublicKey publicKey
) {
}