package com.luvine.api.exception;

import com.luvine.common.domain.BusinessRuleValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessRuleValidationException.class)
    public ResponseEntity<ApiError> handleBusinessRuleException(
            BusinessRuleValidationException ex, HttpServletRequest request) {

        log.warn("event=business_rule_broken rule={} path={}",
                ex.getBrokenRule().getClass().getSimpleName(), request.getRequestURI());

        return buildError(HttpStatus.CONFLICT, ex.getMessage(), null, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.warn("event=validation_failed path={} fields={}", request.getRequestURI(), details);

        return buildError(HttpStatus.BAD_REQUEST, "Erro de validação.", details, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(HttpServletRequest request) {

        log.warn("event=entity_not_found path={}", request.getRequestURI());

        return buildError(HttpStatus.NOT_FOUND, "Entidade não encontrada.", null, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(HttpServletRequest request) {

        log.warn("event=authentication_failed path={}", request.getRequestURI());

        return buildError(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.", null, request);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiError> handleDenied(HttpServletRequest request) {

        log.warn("event=authorization_denied path={}", request.getRequestURI());

        return buildError(HttpStatus.FORBIDDEN, "Acesso negado.", null, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {

        log.error("event=unhandled_exception path={} type={}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex);

        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado.", null, request);
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status, String message,
            List<String> details, HttpServletRequest request) {

        ApiError apiError = ApiError.create(
                status.value(),
                status.getReasonPhrase(),
                message,
                details,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(apiError);
    }
}