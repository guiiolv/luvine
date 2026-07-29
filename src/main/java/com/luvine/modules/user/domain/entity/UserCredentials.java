package com.luvine.modules.user.domain.entity;

import com.luvine.common.domain.AggregateRoot;
import com.luvine.modules.user.domain.rules.*;
import com.luvine.modules.user.domain.valueobject.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "user_credentials", schema = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserCredentials extends AggregateRoot<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_credentials_seq")
    @SequenceGenerator(name = "user_credentials_seq", sequenceName = "users.user_credentials_id_seq")
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "email", nullable = false, updatable = false)
    )
    private Email email;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "first_name", nullable = false, length = 100)
    )
    private FirstName firstName;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "last_name", nullable = false, length = 100)
    )
    private LastName lastName;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "hashed_password", nullable = false)
    )
    private HashedPassword hashedPassword;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "last_verification_email_sent_at")
    private Instant lastVerificationEmailSentAt;

    @Column(name = "verification_email_request_count", nullable = false)
    private int verificationEmailRequestCount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public Long getId() {
        return id;
    }

    private UserCredentials(
            UUID publicId,
            Email email,
            FirstName firstName,
            LastName lastName,
            HashedPassword hashedPassword) {
        this.publicId = publicId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashedPassword = hashedPassword;
        this.role = Role.CUSTOMER;
        this.active = true;
        this.emailVerified = false;
        this.lastVerificationEmailSentAt = null;
        this.verificationEmailRequestCount = 0;
    }

    public static UserCredentials create(
            UUID publicId,
            Email email,
            FirstName firstName,
            LastName lastName,
            HashedPassword hashedPassword) {
        return new UserCredentials(publicId, email, firstName, lastName, hashedPassword);
    }

    public void changeFirstName(FirstName newFirstName) {
        checkRule(new FirstNameMustBeDifferentRule(this.firstName, newFirstName));
        this.firstName = newFirstName;
    }

    public void changeLastName(LastName newLastName) {
        checkRule(new LastNameMustBeDifferentRule(this.lastName, newLastName));
        this.lastName = newLastName;
    }

    public void changeHashedPassword(HashedPassword newHashedPassword) {
        this.hashedPassword = newHashedPassword;
    }

    public void changeRole(Role newRole) {
        checkRule(new RoleMustBeDifferentRule(this.role, newRole));
        this.role = newRole;
    }

    public void markEmailAsVerified() {
        checkRule(new EmailMustNotBeVerifiedRule(this.emailVerified));
        this.emailVerified = true;
    }

    public void markVerificationEmailSent() {
        this.lastVerificationEmailSentAt = Instant.now();
        this.verificationEmailRequestCount++;
    }

    public void resetEmailVerificationRequests() {
        this.verificationEmailRequestCount = 0;
    }

    public void requestEmailVerification(Instant now) {
        if (this.lastVerificationEmailSentAt != null &&
                this.lastVerificationEmailSentAt.isBefore(now.minus(1, ChronoUnit.HOURS))) {
            resetEmailVerificationRequests();
            this.lastVerificationEmailSentAt = null;
        }

        Instant nextAllowedRequest = calculateNextAllowedRequest();

        checkRule(new VerificationEmailRequestMustRespectRateLimitRule(now, nextAllowedRequest));

        markVerificationEmailSent();
    }

    public void activate() {
        checkRule(new UserMustBeInactiveRule(this.active));
        this.active = true;
    }

    public void deactivate() {
        checkRule(new UserMustBeActiveRule(this.active));
        this.active = false;
    }

    private Instant calculateNextAllowedRequest() {
        if (this.lastVerificationEmailSentAt == null) return null;

        long delaySeconds = (this.verificationEmailRequestCount + 1) * 120L;

        return this.lastVerificationEmailSentAt.plusSeconds(delaySeconds);
    }
}