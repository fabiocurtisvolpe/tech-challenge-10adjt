package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtivarInativarUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtivarInativarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static AtivarInativarUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(String email, Boolean ativo) {
        
        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(email).orElse(null);
        
        if (usuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Usuario novoUsuario = UsuarioFactory.usuario(usuarioExistente.getId(), usuarioExistente.getNome(), 
        usuarioExistente.getEmail(), usuarioExistente.getSenha(), 
        usuarioExistente.getTipoUsuario(), usuarioExistente.getEnderecos(), ativo);
    
        return usuarioRepository.atualizar(novoUsuario);
    }
}
