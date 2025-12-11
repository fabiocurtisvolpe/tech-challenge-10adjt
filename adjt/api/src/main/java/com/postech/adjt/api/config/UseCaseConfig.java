package com.postech.adjt.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.PaginadoUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.AtualizarTipoUsuarioUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.CadastrarTipoUsuarioUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.ObterTipoUsuarioPorIdUseCase;
import com.postech.adjt.domain.usecase.usuario.AtivarInativarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarSenhaUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.CadastrarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorEmailUseCase;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorIdUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return AtivarInativarUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return AtualizarSenhaUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return AtualizarUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public CadastrarUsuarioUseCase cadastrarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return CadastrarUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return ObterUsuarioPorEmailUseCase.create(usuarioRepository);
    }

    @Bean
    public ObterUsuarioPorIdUseCase obterUsuarioPorIdUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return ObterUsuarioPorIdUseCase.create(usuarioRepository);
    }

    @Bean
    public PaginadoUseCase<Usuario> paginadoUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return PaginadoUseCase.create(usuarioRepository);
    }

    /*************************************************************************************************************/

    @Bean
    public AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return AtualizarTipoUsuarioUseCase.create(tipoUsuarioRepository);
    }

    @Bean
    public CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return CadastrarTipoUsuarioUseCase.create(tipoUsuarioRepository);
    }

    @Bean
    public ObterTipoUsuarioPorIdUseCase obterTipoUsuarioPorIdUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return ObterTipoUsuarioPorIdUseCase.create(tipoUsuarioRepository);
    }

    @Bean
    public PaginadoUseCase<TipoUsuario> paginadoTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return PaginadoUseCase.create(tipoUsuarioRepository);
    }
}