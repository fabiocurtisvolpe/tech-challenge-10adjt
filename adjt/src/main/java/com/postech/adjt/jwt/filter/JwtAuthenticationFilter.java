package com.postech.adjt.jwt.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.postech.adjt.jwt.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autenticação JWT que intercepta todas as requisições HTTP.
 *
 * <p>
 * Esse filtro verifica o cabeçalho "Authorization" em busca de um token JWT
 * válido.
 * Caso o token seja válido e o usuário esteja autenticado, o contexto de
 * segurança
 * é atualizado com as credenciais do usuário.
 * </p>
 *
 * <p>
 * Extende {@link OncePerRequestFilter} para garantir que o filtro seja
 * executado
 * apenas uma vez por requisição.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Serviço responsável por operações com JWT, como extração de usuário e
     * validação de token.
     */
    private final JwtService jwtService;

    /**
     * Serviço que carrega os dados do usuário com base no nome de usuário extraído
     * do token.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Construtor que injeta os serviços necessários para autenticação JWT.
     *
     * @param jwtService         Serviço de manipulação de tokens JWT.
     * @param userDetailsService Serviço de carregamento de dados do usuário.
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método principal do filtro que intercepta a requisição HTTP.
     *
     * <p>
     * Verifica se o cabeçalho "Authorization" contém um token JWT válido.
     * Se o token for válido e o usuário não estiver autenticado, carrega os dados
     * do usuário e atualiza o contexto de segurança.
     * </p>
     *
     * @param request     Requisição HTTP.
     * @param response    Resposta HTTP.
     * @param filterChain Cadeia de filtros.
     * @throws ServletException Em caso de erro na filtragem.
     * @throws IOException      Em caso de erro de I/O.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}