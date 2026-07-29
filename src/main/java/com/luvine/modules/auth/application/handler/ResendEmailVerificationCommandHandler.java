package com.luvine.modules.auth.application.handler;

import com.luvine.common.domain.exception.UnauthorizedException;
import com.luvine.common.domain.util.EmailMaskUtil;
import com.luvine.modules.auth.application.command.ResendEmailVerificationCommand;
import com.luvine.modules.auth.domain.entity.EmailVerification;
import com.luvine.modules.auth.domain.rules.CodeHash;
import com.luvine.modules.auth.infrastructure.repository.EmailVerificationRepository;
import com.luvine.modules.notification.application.command.SendEmailVerificationCodeCommand;
import com.luvine.modules.notification.application.handler.SendEmailVerificationCodeCommandHandler;
import com.luvine.modules.user.domain.entity.UserCredentials;
import com.luvine.modules.user.domain.valueobject.Email;
import com.luvine.modules.user.infrastructure.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
public class ResendEmailVerificationCommandHandler {

    private final UserCredentialsRepository credentialsRepository;
    private final EmailVerificationRepository verificationRepository;
    private final SendEmailVerificationCodeCommandHandler verificationCodeCommandHandler;

    public ResendEmailVerificationCommandHandler(
            UserCredentialsRepository credentialsRepository,
            EmailVerificationRepository verificationRepository,
            SendEmailVerificationCodeCommandHandler verificationCodeCommandHandler) {
        this.credentialsRepository = credentialsRepository;
        this.verificationRepository = verificationRepository;
        this.verificationCodeCommandHandler = verificationCodeCommandHandler;
    }

    @Transactional
    public void handle(ResendEmailVerificationCommand command) {
        String maskedEmail = EmailMaskUtil.mask(command.email());


        UserCredentials credentials = credentialsRepository.findByEmail(new Email(command.email()))
                .orElseThrow(() -> {
                    log.warn(
                            "Tentativa de reenviar código de verificação para um usuário inexistente. E-mail: {}",
                            maskedEmail
                    );
                    return new UnauthorizedException("Credenciais inválidas.");
                });

        String rawCode = CodeHash.generateRawCode();

        credentials.requestEmailVerification(Instant.now());

        credentialsRepository.save(credentials);

        EmailVerification verification = EmailVerification.create(
                credentials.getPublicId(),
                CodeHash.fromRawCode(rawCode)
        );

        verificationRepository.save(verification);

        log.info(
                "Novo código de verificação gerado com sucesso. Usuário: {}",
                credentials.getPublicId()
        );

        verificationCodeCommandHandler.handle(new SendEmailVerificationCodeCommand(
                credentials.getEmail().value(),
                credentials.getFirstName().value(),
                rawCode
        ));

        log.info(
                "Solicitação de reenvio do e-mail de verificação concluída. Usuário: {}",
                credentials.getPublicId()
        );
    }
}