package com.postech.adjt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manipulador personalizado para falhas de autenticação no Spring Security.
 *
 * <p>
 * Intercepta exceções do tipo {@link AuthenticationException} e retorna uma
 * resposta JSON estruturada com informações sobre o erro, como timestamp,
 * status HTTP, mensagem e caminho da requisição.
 * </p>
 *
 * <p>
 * Esse componente é utilizado quando o usuário não está autenticado ou
 * fornece um token inválido, sendo integrado à configuração de segurança
 * via {@code http.exceptionHandling().authenticationEntryPoint(...)}.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Utilitário para serialização de objetos em JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Trata exceções de autenticação e retorna uma resposta JSON estruturada.
     *
     * <p>
     * Define o status HTTP como 401 (Unauthorized), o tipo de conteúdo como JSON
     * e inclui detalhes como timestamp, mensagem de erro e URI da requisição.
     * </p>
     *
     * @param request       Requisição HTTP que gerou o erro.
     * @param response      Resposta HTTP a ser enviada.
     * @param authException Exceção capturada pelo Spring Security.
     * @throws IOException      Em caso de erro na escrita da resposta.
     * @throws ServletException Em caso de erro no processamento do servlet.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", "Token inválido ou não fornecido");
        body.put("path", request.getRequestURI());

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}