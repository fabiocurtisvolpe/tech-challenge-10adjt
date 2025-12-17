package com.postech.adjt.domain.usecase.util;

import java.util.Objects;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class UsuarioLogadoUtil {

    public static Usuario usuarioLogado(GenericRepositoryPort<Usuario> usuarioRepository, String email) {

        if (Objects.isNull(email)) {
             throw new NotificacaoException(MensagemUtil.EMAIL_NULO);
        }

        return usuarioRepository.obterPorEmail(email)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));
    }
}
