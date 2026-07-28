package com.luvine.modules.auth.application.handler;

import com.luvine.common.domain.exception.UnauthorizedException;
import com.luvine.common.domain.util.EmailMaskUtil;
import com.luvine.modules.auth.application.command.LoginCommand;
import com.luvine.modules.auth.application.dto.AuthTokensDto;
import com.luvine.modules.auth.infrastructure.service.JwtService;
import com.luvine.modules.user.domain.entity.UserCredentials;
import com.luvine.modules.user.domain.valueobject.Email;
import com.luvine.modules.user.infrastructure.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginCommandHandler {

    private final UserCredentialsRepository credentialsRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginCommandHandler(
            UserCredentialsRepository credentialsRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.credentialsRepository = credentialsRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthTokensDto handle(LoginCommand command) {
        String maskedEmail = EmailMaskUtil.mask(command.email());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    command.email(),
                    command.password()
            ));
        } catch (AuthenticationException ex) {
            log.warn(
                    "Falha na autenticação. E-mail: {}",
                    maskedEmail
            );
            throw new UnauthorizedException("Credenciais inválidas.");
        }

        UserCredentials credentials = credentialsRepository.findByEmail(new Email(command.email()))
                .orElseThrow(() -> {
                    log.error(
                            "Usuário autenticado não foi encontrado no repositório. E-mail: {}",
                            maskedEmail
                    );
                    return new UnauthorizedException("Credenciais inválidas.");
                });

        String accessToken = jwtService.generateAccessToken(
                credentials.getPublicId(),
                credentials.getRole()
        );

        String refreshToken = jwtService.generateRefreshToken(
                credentials.getPublicId(),
                command.deviceInfo(),
                command.ipAddress()
        );

        log.info(
                "Login realizado com sucesso. Usuário: {}",
                credentials.getPublicId()
        );

        return new AuthTokensDto(accessToken, refreshToken);
    }
}