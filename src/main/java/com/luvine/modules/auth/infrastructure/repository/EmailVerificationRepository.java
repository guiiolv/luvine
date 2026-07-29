package com.luvine.modules.auth.infrastructure.repository;

import com.luvine.modules.auth.domain.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification>
    findFirstByUserPublicIdAndVerifiedAtIsNullAndInvalidatedAtIsNullOrderByCreatedAtDesc(UUID userPublicId);

    @Modifying
    @Query("UPDATE EmailVerification e SET e.invalidatedAt = CURRENT_TIMESTAMP WHERE " +
            "e.userPublicId = :userPublicId " +
            "AND e.invalidatedAt IS NULL " +
            "AND e.verifiedAt IS NULL " +
            "AND e.expiresAt > CURRENT_TIMESTAMP")
    void invalidateAllPendingCodes(@Param("userPublicId") UUID userPublicId);

    @Modifying
    @Query("DELETE FROM EmailVerification e WHERE e.expiresAt < :now")
    void deleteExpiredCodes(@Param("now")Instant now);
}