package com.postech.adjt.domain.usecase.usuario;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

public class ObterUsuarioPorEmailUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    private ObterUsuarioPorEmailUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static ObterUsuarioPorEmailUseCase create(UsuarioRepositoryPort usuarioRepository) {
        return new ObterUsuarioPorEmailUseCase(usuarioRepository);
    }

    public Optional<Usuario> run(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.EMAIL_NULO);
        }
        
        Optional<Usuario> usuarioExistente = this.usuarioRepository.obterPorEmail(email);
        
        if (usuarioExistente.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        return usuarioExistente;
    }
 
}
