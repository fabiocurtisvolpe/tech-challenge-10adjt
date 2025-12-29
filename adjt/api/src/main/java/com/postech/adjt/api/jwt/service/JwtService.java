package com.postech.adjt.api.jwt.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 *
 * <p>
 * Utiliza a biblioteca {@link io.jsonwebtoken.Jwts} para criar tokens
 * assinados com algoritmo HMAC SHA-256, e extrair informações como login
 * e data de expiração.
 * </p>
 *
 * <p>
 * Esse serviço é utilizado no processo de autenticação para garantir
 * que o token recebido seja válido e corresponda ao usuário autenticado.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Service
public class JwtService {

    /**
     * Chave secreta utilizada para assinar e validar tokens JWT.
     */
    private static final String SECRET = "67a0d7fda0e71a136b5d3deada701db7bd9c6fe9380333012b9145e0549ccd7d";

    /**
     * Instância da chave criptográfica derivada da constante {@code SECRET}.
     */
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Gera um token JWT para o usuário autenticado.
     *
     * <p>
     * O token inclui o login do usuário como {@code subject}, a data de emissão
     * e uma expiração de 1 hora.
     * </p>
     *
     * @param userDetails Detalhes do usuário autenticado.
     * @return Token JWT assinado.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1)) // 1h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Verifica se o token JWT é válido para o usuário informado.
     *
     * <p>
     * Valida se o login contido no token corresponde ao usuário e se o token
     * não está expirado.
     * </p>
     *
     * @param token       Token JWT recebido.
     * @param userDetails Detalhes do usuário autenticado.
     * @return {@code true} se o token for válido.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Extrai o login do usuário contido no token JWT.
     *
     * @param token Token JWT.
     * @return Login do usuário.
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extrai a data de expiração do token JWT.
     *
     * @param token Token JWT.
     * @return Data de expiração.
     */
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    /**
     * Verifica se o token JWT está expirado.
     *
     * @param token Token JWT.
     * @return {@code true} se o token estiver expirado.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai todas as {@link Claims} contidas no token JWT.
     *
     * @param token Token JWT.
     * @return Objeto {@link Claims} com os dados do token.
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}