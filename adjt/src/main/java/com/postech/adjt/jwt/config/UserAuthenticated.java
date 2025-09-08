package com.postech.adjt.jwt.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.postech.adjt.model.Usuario;

import java.util.Collection;
import java.util.List;

/**
 * Implementação da interface {@link UserDetails} baseada na entidade
 * {@link Usuario}.
 *
 * <p>
 * Essa classe é utilizada pelo Spring Security para representar o usuário
 * autenticado
 * no contexto de segurança da aplicação.
 * </p>
 *
 * <p>
 * Os métodos fornecem informações como login, senha e status da conta,
 * conforme exigido pelo framework.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
public class UserAuthenticated implements UserDetails {

    /**
     * Instância da entidade {@link Usuario} que representa o usuário autenticado.
     */
    private final Usuario usuario;

    /**
     * Construtor que recebe a entidade {@link Usuario}.
     *
     * @param usuario Usuário autenticado.
     */
    public UserAuthenticated(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Retorna as autoridades concedidas ao usuário.
     *
     * <p>
     * Atualmente retorna uma lista vazia, podendo ser estendida para incluir
     * perfis ou permissões.
     * </p>
     *
     * @return Lista de {@link GrantedAuthority}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Retorna a senha do usuário.
     *
     * @return Senha codificada.
     */
    @Override
    public String getPassword() {
        return this.usuario.getSenha();
    }

    /**
     * Retorna o nome de usuário (login).
     *
     * @return Login do usuário.
     */
    @Override
    public String getUsername() {
        return this.usuario.getLogin();
    }

    /**
     * Indica se a conta está expirada.
     *
     * @return {@code true} se a conta não estiver expirada.
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Indica se a conta está bloqueada.
     *
     * @return {@code true} se a conta não estiver bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Indica se as credenciais estão expiradas.
     *
     * @return {@code true} se as credenciais não estiverem expiradas.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Indica se o usuário está habilitado.
     *
     * @return {@code true} se o usuário estiver habilitado.
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}