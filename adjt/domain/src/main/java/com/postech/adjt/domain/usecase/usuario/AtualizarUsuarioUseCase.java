package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.validators.UsuarioValidator;

public class AtualizarUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtualizarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static AtualizarUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(UsuarioDTO dto) {
        
        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(dto.email()).orElse(null);
        
        if (usuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Usuario usuario = UsuarioFactory.usuario(usuarioExistente.getId(), usuarioExistente.getNome(), 
        usuarioExistente.getEmail(),  usuarioExistente.getSenha(), 
        usuarioExistente.getTipoUsuario(), usuarioExistente.getEnderecos(), true);

        UsuarioValidator.validarParaAtualizacao(usuario);

        return usuarioRepository.atualizar(usuario);
    }
}
