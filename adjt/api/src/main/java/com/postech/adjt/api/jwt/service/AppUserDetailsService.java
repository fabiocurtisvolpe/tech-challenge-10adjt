package com.postech.adjt.api.jwt.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.postech.adjt.data.entity.UsuarioEntity;
import com.postech.adjt.data.repository.UsuarioRepository;
import com.postech.adjt.domain.model.Usuario;

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
 * Utiliza o {@link UsuarioRepository} para buscar o usuário no banco de dados
 * com base no login informado.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

        /**
         * Repositório de acesso à entidade {@link Usuario}.
         */
        private final UsuarioRepository usuarioRepository;

        /**
         * Construtor que injeta o repositório de usuários.
         *
         * @param usuarioRepository Repositório de usuários.
         */
        public AppUserDetailsService(UsuarioRepository usuarioRepository) {
                this.usuarioRepository = usuarioRepository;
        }

        /**
         * Carrega os dados do usuário com base no login informado.
         *
         * <p>
         * Busca o usuário no banco de dados e, se encontrado, retorna uma instância
         * de {@link User} com e-mail, senha e lista de autoridades (vazia por padrão).
         * </p>
         *
         * @param login E-mail de usuário informado no processo de autenticação.
         * @return Detalhes do usuário para autenticação.
         * @throws UsernameNotFoundException Se o usuário não for encontrado.
         */
        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

                UsuarioEntity usuario = this.usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

                /*
                 * SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                 * "ROLE_" + usuario.getTipoUsuario().replace(" ", "_").toUpperCase());
                 * 
                 * return new User(
                 * usuario.getEmail(),
                 * usuario.getSenha(),
                 * Collections.singletonList(authority));
                 */

                return new User(
                                usuario.getEmail(),
                                usuario.getSenha(), null);
        }
}