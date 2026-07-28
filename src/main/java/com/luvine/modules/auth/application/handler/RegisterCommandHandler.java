package com.luvine.modules.auth.application.handler;

import com.luvine.common.domain.exception.UnauthorizedException;
import com.luvine.common.domain.util.EmailMaskUtil;
import com.luvine.modules.auth.application.command.RegisterCommand;
import com.luvine.modules.auth.application.command.RequestEmailVerificationCommand;
import com.luvine.modules.notification.application.command.SendEmailVerificationCodeCommand;
import com.luvine.modules.notification.application.handler.SendEmailVerificationCodeCommandHandler;
import com.luvine.modules.user.domain.entity.UserCredentials;
import com.luvine.modules.user.domain.valueobject.Email;
import com.luvine.modules.user.domain.valueobject.FirstName;
import com.luvine.modules.user.domain.valueobject.HashedPassword;
import com.luvine.modules.user.domain.valueobject.LastName;
import com.luvine.modules.user.infrastructure.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class RegisterCommandHandler {

    private final UserCredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final RequestEmailVerificationCommandHandler verificationCommandHandler;
    private final SendEmailVerificationCodeCommandHandler verificationCodeCommandHandler;

    public RegisterCommandHandler(
            UserCredentialsRepository credentialsRepository,
            PasswordEncoder passwordEncoder,
            RequestEmailVerificationCommandHandler verificationCommandHandler
            , SendEmailVerificationCodeCommandHandler verificationCodeCommandHandler) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationCommandHandler = verificationCommandHandler;
        this.verificationCodeCommandHandler = verificationCodeCommandHandler;
    }

    @Transactional
    public void handle(RegisterCommand command) {
        String maskedEmail = EmailMaskUtil.mask(command.email());

        log.info("Iniciando cadastro de usuário. E-mail: {}", maskedEmail);

        if (credentialsRepository.existsByEmail(new Email(command.email()))) {
            log.warn(
                    "Tentativa de cadastro com e-mail já utilizado. E-mail: {}",
                    maskedEmail
            );
            throw new UnauthorizedException("Email já está em uso.");
        }

        UUID publicId = UUID.randomUUID();
        HashedPassword hashedPassword = new HashedPassword(passwordEncoder.encode(command.hashedPassword()));

        UserCredentials credentials = UserCredentials.create(
                publicId,
                new Email(command.email()),
                new FirstName(command.firstName()),
                new LastName(command.lastName()),
                hashedPassword
        );

        credentialsRepository.save(credentials);

        log.info(
                "Usuário cadastrado com sucesso. Id público: {}, E-mail: {}",
                publicId,
                maskedEmail
        );

        log.info(
                "Gerando código de verificação de e-mail. Usuário: {}",
                publicId
        );

        String rawCode = verificationCommandHandler.handle(new RequestEmailVerificationCommand(command.email()));

        log.info(
                "Solicitando envio do e-mail de verificação. Usuário: {}, E-mail: {}",
                publicId,
                maskedEmail
        );

        verificationCodeCommandHandler.handle(new SendEmailVerificationCodeCommand(
                command.email(),
                command.firstName(),
                rawCode
        ));

        log.info(
                "Fluxo de cadastro concluído. Usuário: {}",
                publicId
        );
    }
}