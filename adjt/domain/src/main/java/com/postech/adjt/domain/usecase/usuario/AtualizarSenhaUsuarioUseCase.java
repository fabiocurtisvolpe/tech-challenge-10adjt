package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

public class AtualizarSenhaUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    private AtualizarSenhaUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static AtualizarSenhaUsuarioUseCase create(UsuarioRepositoryPort usuarioRepository) {
        return new AtualizarSenhaUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(TrocarSenhaUsuarioDTO dto) {
        
        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(dto.email()).orElse(null);
        
        if (usuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Usuario usuario = Usuario.atualizar(
            usuarioExistente.getId(),
            usuarioExistente.getNome(),
            usuarioExistente.getEmail(),
            dto.senhaCodificada(),
            usuarioExistente.getTipoUsuario(),
            usuarioExistente.getEnderecos(),
            true
        );

        return usuarioRepository.atualizar(usuario);
    }
 
}
