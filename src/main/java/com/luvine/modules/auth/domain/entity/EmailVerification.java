package com.luvine.modules.auth.domain.entity;

import com.luvine.common.domain.AggregateRoot;
import com.luvine.modules.auth.domain.rules.CodeHash;
import com.luvine.modules.auth.domain.rules.CodeMustMatchRule;
import com.luvine.modules.auth.domain.rules.CodeMustNotBeExpiredRule;
import com.luvine.modules.auth.domain.rules.CodeMustNotBeVerifiedRule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "email_verifications", schema = "auth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification extends AggregateRoot<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verifications_seq")
    @SequenceGenerator(name = "email_verifications_seq", sequenceName = "email_verifications_id_seq")
    private Long id;

    @Column(name = "user_public_id", nullable = false, updatable = false)
    private UUID userPublicId;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "verification_code", nullable = false, updatable = false)
    )
    private CodeHash verificationCode;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private Instant expiresAt;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "invalidatedAt")
    private Instant invalidatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Override
    public Long getId() {
        return id;
    }

    private EmailVerification(UUID userPublicId, CodeHash verificationCode) {
        this.userPublicId = userPublicId;
        this.verificationCode = verificationCode;
        this.createdAt = Instant.now();
        this.expiresAt = this.createdAt.plus(15, ChronoUnit.MINUTES);
        this.invalidatedAt = null;
        this.verifiedAt = null;
    }

    public static EmailVerification create(UUID userPublicId, CodeHash verificationCode) {
        return new EmailVerification(userPublicId, verificationCode);
    }

    public void verify(CodeHash providedCode) {
        checkRule(new CodeMustNotBeExpiredRule(this.expiresAt));
        checkRule(new CodeMustNotBeVerifiedRule(this.verifiedAt));
        checkRule(new CodeMustMatchRule(this.verificationCode, providedCode));
        this.verifiedAt = Instant.now();
    }
}