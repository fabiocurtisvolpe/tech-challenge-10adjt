package com.postech.adjt.api.jwt.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.postech.adjt.data.repository.UsuarioRepositoryAdapter;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorEmailUseCase;

/**
 * Serviço responsável por carregar os dados do usuário para autenticação.
 *
 * <p>
 * Implementa a interface {@link UserDetailsService} do Spring Security,
 * permitindo que o framework recupere informações de login e senha a partir
 * da entidade {@link Usuario}.
 * </p>
 *
 * <p>
 * Utiliza o {@link UsuarioRepositoryAdapter} para buscar o usuário no banco de
 * dados
 * com base no login informado.
 * </p>
 *
 * @author Fabio
 * @since 2025-11-26
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

        private final ObterUsuarioPorEmailUseCase obterUsuarioPorEmail;

        public AppUserDetailsService(ObterUsuarioPorEmailUseCase obterUsuarioPorEmail) {
                this.obterUsuarioPorEmail = obterUsuarioPorEmail;
        }

        /**
         * Carrega os detalhes do usuário com base no email fornecido.
         */
        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

                Optional<Usuario> usuario = this.obterUsuarioPorEmail.run(email);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                                "ROLE_" + usuario.get().getTipoUsuario().toString());

                return new User(
                                usuario.get().getEmail(),
                                usuario.get().getSenha(),
                                Collections.singletonList(authority));
        }
}