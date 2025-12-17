package com.postech.adjt.api.jwt.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorEmailUseCase;

/**
 * Serviço personalizado para carregar os detalhes do usuário com base no email.
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

                Usuario usuario = this.obterUsuarioPorEmail.run(email);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                                "ROLE_" + usuario.getTipoUsuario().toString());

                return new User(
                                usuario.getEmail(),
                                usuario.getSenha(),
                                Collections.singletonList(authority));
        }
}