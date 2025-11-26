package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.NovoUsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

public class CadastrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    private CadastrarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static CadastrarUsuarioUseCase create(UsuarioRepositoryPort usuarioRepository) {
        return new CadastrarUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(NovoUsuarioDTO dto) {
        
        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(dto.email()).orElse(null);
        
        if (usuarioExistente != null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_EXISTENTE);
        }

        final Usuario novoUsuario = Usuario.create(
            dto.nome(),
            dto.email(),
            dto.senha(),
            dto.tipoUsuario(),
            dto.enderecos()
        );

        return usuarioRepository.criar(novoUsuario);
    }
 
}
