package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterUsuarioPorEmailUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private ObterUsuarioPorEmailUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static ObterUsuarioPorEmailUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterUsuarioPorEmailUseCase(usuarioRepository);
    }

    public Usuario run(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.EMAIL_NULO);
        }

        return this.usuarioRepository.obterPorEmail(email)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));
    }

}
