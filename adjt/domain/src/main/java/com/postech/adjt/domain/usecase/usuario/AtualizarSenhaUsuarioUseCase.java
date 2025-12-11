package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtualizarSenhaUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;
    
    private AtualizarSenhaUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static AtualizarSenhaUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarSenhaUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(TrocarSenhaUsuarioDTO dto) {
        
        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(dto.email()).orElse(null);
        
        if (usuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Usuario usuario = UsuarioFactory.usuario(usuarioExistente.getId(), usuarioExistente.getNome(), 
        usuarioExistente.getEmail(),  dto.senhaCodificada(), 
        usuarioExistente.getTipoUsuario(), usuarioExistente.getEnderecos(), true);

        return usuarioRepository.atualizar(usuario);
    }
 
}
