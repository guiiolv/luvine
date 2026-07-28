package com.luvine.modules.auth.application.handler;

import com.luvine.common.domain.exception.InvalidEmailVerificationException;
import com.luvine.common.domain.exception.UnauthorizedException;
import com.luvine.modules.auth.application.command.EmailVerifyCommand;
import com.luvine.modules.auth.domain.entity.EmailVerification;
import com.luvine.modules.auth.domain.rules.CodeHash;
import com.luvine.modules.auth.infrastructure.repository.EmailVerificationRepository;
import com.luvine.modules.user.domain.entity.UserCredentials;
import com.luvine.modules.user.domain.valueobject.Email;
import com.luvine.modules.user.infrastructure.repository.UserCredentialsRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmailVerifyCommandHandler {

    private final UserCredentialsRepository credentialsRepository;
    private final EmailVerificationRepository verificationRepository;

    public EmailVerifyCommandHandler(
            UserCredentialsRepository credentialsRepository,
            EmailVerificationRepository verificationRepository) {
        this.credentialsRepository = credentialsRepository;
        this.verificationRepository = verificationRepository;
    }

    @Transactional
    public void handle(EmailVerifyCommand command) {
        UserCredentials credentials = credentialsRepository.findByEmail(new Email(command.email()))
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas."));

        EmailVerification verification = verificationRepository
                .findFirstByUserPublicIdAndVerifiedAtIsNullAndInvalidatedAtIsNullOrderByCreatedAtDesc(
                        credentials.getPublicId())
                .orElseThrow(() -> new InvalidEmailVerificationException("Código inválido ou expirado."));

        verification.verify(CodeHash.fromRawCode(command.code()));

        credentials.markEmailAsVerified();
        credentials.resetEmailVerificationRequests();

        verificationRepository.save(verification);
        credentialsRepository.save(credentials);
    }
}