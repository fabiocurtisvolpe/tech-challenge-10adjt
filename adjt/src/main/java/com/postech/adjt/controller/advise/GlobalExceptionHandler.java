package com.postech.adjt.controller.advise;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.postech.adjt.exception.NotificacaoException;

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
}