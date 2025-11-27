package com.postech.adjt.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.postech.adjt.domain.ports.UsuarioRepositoryPort;
import com.postech.adjt.domain.usecase.usuario.AtivarInativarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarSenhaUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.CadastrarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorEmailUseCase;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorIdUseCase;
import com.postech.adjt.domain.usecase.usuario.PaginadoUsuarioUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        return AtivarInativarUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        return AtualizarSenhaUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        return AtualizarUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        return CadastrarUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase(UsuarioRepositoryPort usuarioRepository) {
        return ObterUsuarioPorEmailUseCase.create(usuarioRepository);
    }

    @Bean
    public ObterUsuarioPorIdUseCase obterUsuarioPorIdUseCase(UsuarioRepositoryPort usuarioRepository) {
        return ObterUsuarioPorIdUseCase.create(usuarioRepository);
    }

    @Bean
    public PaginadoUsuarioUseCase paginadoUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        return PaginadoUsuarioUseCase.create(usuarioRepository);
    }
}