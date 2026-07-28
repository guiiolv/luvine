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

    Optional<EmailVerification> findFirstByUserPublicIdAndVerifiedAtIsNullOrderByCreatedAtDesc(UUID userPublicId);

    @Modifying
    @Query("DELETE FROM EmailVerification e WHERE e.expiresAt < :now AND e.verifiedAt IS NOT NULL")
    void deleteExpiredCodes(@Param("now")Instant now);
}