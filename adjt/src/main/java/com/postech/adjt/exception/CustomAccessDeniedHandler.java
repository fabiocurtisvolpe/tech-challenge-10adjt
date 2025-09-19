package com.postech.adjt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manipulador personalizado para acessos negados no Spring Security.
 *
 * <p>
 * Intercepta exceções do tipo {@link AccessDeniedException} e retorna uma
 * resposta JSON padronizada com informações sobre o erro, como timestamp,
 * status HTTP, mensagem e caminho da requisição.
 * </p>
 *
 * <p>
 * Essa classe é registrada como um {@link Component} e pode ser integrada
 * à configuração de segurança via
 * {@code http.exceptionHandling().accessDeniedHandler(...)}.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Utilitário para serialização de objetos em JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Trata exceções de acesso negado e retorna uma resposta JSON estruturada.
     *
     * <p>
     * Define o status HTTP como 403 (Forbidden), o tipo de conteúdo como JSON
     * e inclui detalhes como timestamp, mensagem de erro e URI da requisição.
     * </p>
     *
     * @param request               Requisição HTTP que gerou o erro.
     * @param response              Resposta HTTP a ser enviada.
     * @param accessDeniedException Exceção capturada pelo Spring Security.
     * @throws IOException      Em caso de erro na escrita da resposta.
     * @throws ServletException Em caso de erro no processamento do servlet.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 403);
        body.put("error", "Forbidden");
        body.put("message", "Acesso negado: você não tem permissão para acessar este recurso");
        body.put("path", request.getRequestURI());

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}