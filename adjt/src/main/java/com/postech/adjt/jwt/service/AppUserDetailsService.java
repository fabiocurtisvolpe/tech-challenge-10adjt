package com.postech.adjt.jwt.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.postech.adjt.model.Usuario;
import com.postech.adjt.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AppUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return new User(
                usuario.getLogin(),
                usuario.getSenha(),
                Collections.emptyList());
    }
}