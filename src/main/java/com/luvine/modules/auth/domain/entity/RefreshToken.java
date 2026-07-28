package com.luvine.modules.auth.domain.entity;

import com.luvine.common.domain.AggregateRoot;
import com.luvine.modules.auth.domain.rules.TokenHash;
import com.luvine.modules.auth.domain.rules.TokenMustNotAlreadyBeReplacedRule;
import com.luvine.modules.auth.domain.rules.TokenMustNotBeExpiredRule;
import com.luvine.modules.auth.domain.rules.TokenMustNotBeRevokedRule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens", schema = "auth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends AggregateRoot<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_tokens_seq")
    @SequenceGenerator(name = "refresh_tokens_seq", sequenceName = "auth.refresh_tokens_id_seq")
    private Long id;

    @Column(name = "user_public_id", nullable = false, updatable = false)
    private UUID userPublicId;

    @Embedded
    @AttributeOverride
            (name = "value",
                    column = @Column(name = "token_hash", nullable = false, unique = true, updatable = false)
            )
    private TokenHash tokenHash;

    @Column(name = "device_info", nullable = false, updatable = false)
    private String deviceInfo;

    @Column(name = "ip_address", nullable = false, updatable = false)
    private String ipAddress;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @Column(name = "replaced_by_token_hash")
    private String replacedByTokenHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Override
    public Long getId() {
        return id;
    }

    private RefreshToken(UUID userPublicId, TokenHash tokenHash, String deviceInfo, String ipAddress) {
        this.userPublicId = userPublicId;
        this.tokenHash = tokenHash;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
        this.createdAt = Instant.now();
        this.expiresAt = this.createdAt.plus(7, ChronoUnit.DAYS);
        this.revoked = false;
        this.replacedByTokenHash = null;
    }

    public static RefreshToken create(UUID userPublicId, TokenHash tokenHash, String deviceInfo, String ipAddress) {
        return new RefreshToken(userPublicId, tokenHash, deviceInfo, ipAddress);
    }

    public void revoke() {
        checkRule(new TokenMustNotBeExpiredRule(this.expiresAt));
        checkRule(new TokenMustNotBeRevokedRule(this.revoked));
        this.revoked = true;
    }

    public void markAsReplacedBy(String newTokenHash) {
        checkRule(new TokenMustNotAlreadyBeReplacedRule(this.replacedByTokenHash));
        this.replacedByTokenHash = newTokenHash;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }
}