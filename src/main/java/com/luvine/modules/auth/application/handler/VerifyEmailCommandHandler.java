package com.luvine.modules.auth.application.handler;

import com.luvine.common.domain.exception.InvalidEmailVerificationException;
import com.luvine.common.domain.exception.UnauthorizedException;
import com.luvine.common.domain.util.EmailMaskUtil;
import com.luvine.modules.auth.application.command.VerifyEmailCommand;
import com.luvine.modules.auth.domain.entity.EmailVerification;
import com.luvine.modules.auth.domain.rules.CodeHash;
import com.luvine.modules.auth.infrastructure.repository.EmailVerificationRepository;
import com.luvine.modules.user.domain.entity.UserCredentials;
import com.luvine.modules.user.domain.valueobject.Email;
import com.luvine.modules.user.infrastructure.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class VerifyEmailCommandHandler {

    private final UserCredentialsRepository credentialsRepository;
    private final EmailVerificationRepository verificationRepository;

    public VerifyEmailCommandHandler(
            UserCredentialsRepository credentialsRepository,
            EmailVerificationRepository verificationRepository) {
        this.credentialsRepository = credentialsRepository;
        this.verificationRepository = verificationRepository;
    }

    @Transactional
    public void handle(VerifyEmailCommand command) {
        String maskedEmail = EmailMaskUtil.mask(command.email());

        UserCredentials credentials = credentialsRepository.findByEmail(new Email(command.email()))
                .orElseThrow(() -> {
                    log.warn(
                            "Tentativa de verificação de e-mail para um usuário inexistente. E-mail: {}",
                            maskedEmail
                    );
                    return new UnauthorizedException("Credenciais inválidas.");
                });


        EmailVerification verification = verificationRepository
                .findFirstByUserPublicIdAndVerifiedAtIsNullAndInvalidatedAtIsNullOrderByCreatedAtDesc(
                        credentials.getPublicId())
                .orElseThrow(() -> {
                    log.warn(
                            "Tentativa de verificação sem código válido. Usuário: {}",
                            credentials.getPublicId()
                    );
                    return new InvalidEmailVerificationException("Código inválido ou expirado.");
                });

        verification.verify(CodeHash.fromRawCode(command.code()));

        credentials.markEmailAsVerified();
        credentials.resetEmailVerificationRequests();

        verificationRepository.save(verification);
        credentialsRepository.save(credentials);

        log.info(
                "E-mail verificado com sucesso. Usuário: {}",
                credentials.getPublicId()
        );
    }
}