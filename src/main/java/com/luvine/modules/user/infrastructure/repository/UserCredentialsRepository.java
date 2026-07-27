package com.luvine.modules.user.infrastructure.repository;

import com.luvine.modules.user.domain.entity.UserCredentials;
import com.luvine.modules.user.domain.valueobject.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {

    Optional<UserCredentials> findByPublicId(UUID publicId);

    Optional<UserCredentials> findByEmail(Email email);

    boolean existsByEmail(Email email);
}