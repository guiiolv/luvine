package com.luvine.api.exception;

import com.luvine.common.domain.exception.BusinessRuleValidationException;
import com.luvine.common.domain.exception.CryptographyException;
import com.luvine.common.domain.exception.NotificationDeliveryException;
import com.luvine.common.domain.exception.UnauthorizedException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessRuleValidationException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessRuleValidationException ex, HttpServletRequest request) {

        log.warn(
                "Regra de negócio violada. Método: {}, URI: {}, Motivo: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.of(
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {

        log.warn(
                "Requisição não autorizada. Método: {}, URI: {}, Motivo: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.of(
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(CryptographyException.class)
    public ResponseEntity<ApiError> handleCryptography(CryptographyException ex, HttpServletRequest request) {

        log.error(
                "Falha durante o processamento de uma operação criptográfica. Método: {}, URI: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(NotificationDeliveryException.class)
    public ResponseEntity<ApiError> handleNotification(NotificationDeliveryException ex, HttpServletRequest request) {

        log.error(
                "Falha durante o envio de uma notificação. Método: {}, URI: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Objects.requireNonNullElse(
                                fieldError.getDefaultMessage(),
                                "Valor inválido."
                        ),
                        (existing, replacement) -> replacement
                ));

        log.warn(
                "Falha na validação da requisição. Método: {}, URI: {}, Campos inválidos: {}",
                request.getMethod(),
                request.getRequestURI(),
                errors.keySet()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.ofValidation(
                        "Os dados enviados são inválidos. Verifique os campos e tente novamente.",
                        request.getRequestURI(),
                        errors
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMisMatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        log.warn(
                "Parâmetro inválido recebido. Método: {}, URI: {}, Parâmetro: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getName()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(
                        "Parâmetro inválido.",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleIntegrity(HttpServletRequest request) {


        log.warn(
                "Violação de integridade de dados. Método: {}, URI: {}",
                request.getMethod(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.of(
                        "Não foi possível processar a solicitação. Um registro com esses dados já existe" +
                                " ou viola uma restrição do sistema.",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(HttpServletRequest request) {

        log.warn(
                "Recurso não encontrado. Método: {}, URI: {}",
                request.getMethod(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(
                        "O recurso solicitado não foi encontrado.",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(HttpServletRequest request) {

        log.warn(
                "Falha de autenticação. Método: {}, URI: {}",
                request.getMethod(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.of(
                        "Credenciais inválidas.",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiError> handleAuthorization(HttpServletRequest request) {

        log.warn(
                "Tentativa de acesso sem autorização. Método: {}, URI: {}",
                request.getMethod(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiError.of(
                        "Acesso negado.",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {

        log.error(
                "Erro interno inesperado. Método: {}, URI: {}, Exceção: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getClass().getSimpleName(),
                ex
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(
                        "Ocorreu um erro interno inesperado. Tente novamente mais tarde" +
                                " ou entre em contato com o suporte.",
                        request.getRequestURI()
                ));
    }
}