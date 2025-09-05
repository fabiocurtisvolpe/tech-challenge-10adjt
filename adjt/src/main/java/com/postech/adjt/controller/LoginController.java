package com.postech.adjt.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.jwt.model.AuthRequest;
import com.postech.adjt.jwt.model.AuthResponse;
import com.postech.adjt.jwt.service.AppUserDetailsService;
import com.postech.adjt.jwt.service.JwtService;

/**
 * Controlador responsável pela autenticação de usuários via JWT.
 * 
 * <p>
 * Recebe credenciais de login, autentica o usuário usando o
 * {@link AuthenticationManager},
 * e retorna um token JWT válido para acesso aos recursos protegidos da API.
 * </p>
 * 
 * <p>
 * Utiliza os serviços {@link AppUserDetailsService} para carregar os dados do
 * usuário
 * e {@link JwtService} para geração do token.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-05
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {

    /**
     * Serviço responsável por carregar os dados do usuário para autenticação.
     */
    protected final AppUserDetailsService userDetailsService;

    /**
     * Gerenciador de autenticação do Spring Security.
     */
    protected final AuthenticationManager authManager;

    /**
     * Serviço responsável pela geração de tokens JWT.
     */
    protected final JwtService jwtService;

    /**
     * Construtor com injeção de dependências.
     *
     * @param authManager        Gerenciador de autenticação.
     * @param jwtService         Serviço de geração de tokens JWT.
     * @param userDetailsService Serviço de carregamento de dados do usuário.
     */
    public LoginController(AuthenticationManager authManager, JwtService jwtService,
            AppUserDetailsService userDetailsService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Realiza a autenticação do usuário e retorna um token JWT.
     *
     * <p>
     * Autentica as credenciais fornecidas e, se válidas, gera um token JWT
     * que pode ser usado para acessar endpoints protegidos.
     * </p>
     *
     * @param request Objeto contendo login e senha.
     * @return {@link AuthResponse} contendo o token JWT.
     */
    @PostMapping
    public AuthResponse login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.senha()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.login());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
