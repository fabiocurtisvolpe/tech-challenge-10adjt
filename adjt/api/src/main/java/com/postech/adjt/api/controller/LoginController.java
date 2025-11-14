package com.postech.adjt.api.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.api.jwt.model.LoginRequest;
import com.postech.adjt.api.jwt.model.LoginResponse;
import com.postech.adjt.api.jwt.service.AppUserDetailsService;
import com.postech.adjt.api.jwt.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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

@Tag(name = "Login", description = "API de gerenciamento de login")
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
     * @return {@link LoginResponse} contendo o token JWT.
     */
    @Operation(summary = "Login", description = "Realiza login do usuário e retorna token JWT para autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public LoginResponse login(
            @Parameter(description = "Credenciais de login", required = true) @RequestBody @Valid LoginRequest request) {

        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.login(), request.sehha()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.login());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }
}
