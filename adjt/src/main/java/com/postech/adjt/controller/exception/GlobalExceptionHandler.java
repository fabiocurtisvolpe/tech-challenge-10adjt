package com.postech.adjt.controller.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.postech.adjt.exception.DuplicateEntityException;
import com.postech.adjt.exception.NotificacaoException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Manipulador global de exceções para a aplicação.
 * 
 * <p>
 * Intercepta exceções lançadas pelos controllers e retorna respostas
 * padronizadas
 * com informações úteis para o cliente.
 * </p>
 * 
 * <p>
 * Utiliza a anotação {@link ControllerAdvice} para aplicar tratamento
 * centralizado
 * e estende {@link ResponseEntityExceptionHandler} para herdar comportamentos
 * padrão.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-05
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Trata exceções do tipo {@link NotificacaoException}, geralmente relacionadas
     * a falhas
     * no envio ou processamento de notificações.
     *
     * @param ex      Exceção capturada.
     * @param request Detalhes da requisição que gerou a exceção.
     * @return Resposta com status 400 e corpo detalhado do erro.
     */
    @ExceptionHandler(NotificacaoException.class)
    public ResponseEntity<Object> handleNotificacaoFalhaException(
            NotificacaoException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções do tipo {@link IllegalArgumentException}, geralmente lançadas
     * por argumentos inválidos em chamadas de métodos.
     *
     * @param ex      Exceção capturada.
     * @param request Detalhes da requisição que gerou a exceção.
     * @return Resposta com status 400 e corpo detalhado do erro.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Invalid Argument");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata todas as exceções não mapeadas especificamente.
     *
     * @param ex      Exceção genérica capturada.
     * @param request Detalhes da requisição que gerou a exceção.
     * @return Resposta com status 500 e corpo detalhado do erro.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred: " + ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Sobrescreve o método padrão para tratar erros de validação com @Valid
     * Retorna mensagens personalizadas de validação
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                "Erro de validação",
                "Os dados fornecidos são inválidos",
                HttpStatus.BAD_REQUEST.value(),
                errors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Sobrescreve o método padrão para tratar mensagens não legíveis (JSON
     * malformado)
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        String message = ex.getMessage();
        if (message != null && message.contains("Required request body is missing")) {
            errors.put("body", "Corpo da requisição é obrigatório");
        } else {
            errors.put("json", "JSON inválido ou malformado");
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "Erro na requisição",
                "O corpo da requisição está inválido",
                HttpStatus.BAD_REQUEST.value(),
                errors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Para validação de constraints em parâmetros (não coberto pelo
     * ResponseEntityExceptionHandler)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex) {

        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage));

        ErrorResponse errorResponse = new ErrorResponse(
                "Erro de validação",
                "Os parâmetros fornecidos são inválidos",
                HttpStatus.BAD_REQUEST.value(),
                errors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata exceções de autenticação do Spring Security
     */
    @ExceptionHandler({
            BadCredentialsException.class,
            AuthenticationException.class
    })
    public ResponseEntity<Object> handleAuthenticationException(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", "Credenciais inválidas");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Trata exceções de acesso negado do Spring Security
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", "Acesso negado");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Object> handleDuplicateEntityException(
            DuplicateEntityException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value()); // 409 Conflict
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Record para resposta de erro padronizada
     */
    public record ErrorResponse(
            String title,
            String detail,
            int status,
            Map<String, String> errors) {
    }
}