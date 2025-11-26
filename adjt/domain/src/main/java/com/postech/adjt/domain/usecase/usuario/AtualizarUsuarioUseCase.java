package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.AtualizaUsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

public class AtualizarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    private AtualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static AtualizarUsuarioUseCase create(UsuarioRepositoryPort usuarioRepository) {
        return new AtualizarUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(AtualizaUsuarioDTO dto) {
        
        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(dto.email()).orElse(null);
        
        if (usuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Usuario usuario = Usuario.atualizar(
            usuarioExistente.getId(),
            dto.nome(),
            dto.email(),
            usuarioExistente.getSenha(),
            usuarioExistente.getTipoUsuario(),
            dto.enderecos(),
            true
        );

        return usuarioRepository.atualizar(usuario);
    }
 
}
