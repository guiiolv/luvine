package com.luvine.modules.auth.application.handler;

import com.luvine.common.domain.exception.UnauthorizedException;
import com.luvine.modules.auth.application.command.RequestEmailVerificationCommand;
import com.luvine.modules.auth.domain.entity.EmailVerification;
import com.luvine.modules.auth.domain.rules.CodeHash;
import com.luvine.modules.auth.infrastructure.repository.EmailVerificationRepository;
import com.luvine.modules.user.domain.entity.UserCredentials;
import com.luvine.modules.user.domain.valueobject.Email;
import com.luvine.modules.user.infrastructure.repository.UserCredentialsRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class RequestEmailVerificationCommandHandler {

    private final UserCredentialsRepository credentialsRepository;
    private final EmailVerificationRepository verificationRepository;

    public RequestEmailVerificationCommandHandler(
            UserCredentialsRepository credentialsRepository,
            EmailVerificationRepository verificationRepository) {
        this.credentialsRepository = credentialsRepository;
        this.verificationRepository = verificationRepository;
    }

    @Transactional
    public void handle(RequestEmailVerificationCommand command) {
        UserCredentials credentials = credentialsRepository.findByEmail(new Email(command.email()))
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas."));

        credentials.requestEmailVerification(Instant.now());

        credentialsRepository.save(credentials);

        String rawCode = CodeHash.generateRawCode();

        verificationRepository.invalidateAllPendingCodes(credentials.getPublicId());

        EmailVerification verification = EmailVerification.create(
                credentials.getPublicId(),
                CodeHash.fromRawCode(rawCode)
        );

        verificationRepository.save(verification);
    }
}