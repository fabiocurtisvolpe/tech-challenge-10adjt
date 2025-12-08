package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;

public class TipoCozinhaValidator {

    public static void validar(Usuario usuario, TipoCozinha tipoCozinha) throws NotificacaoException {

        if (tipoCozinha == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_COZINHA_NULO);
        }

        if (tipoCozinha.getNome() == null || tipoCozinha.getNome().trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.TIPO_COZINHA_NULO_VALIDACAO);
        }

        if (usuario == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NULO);
        }

        if (usuario.getIsDono() == null || !usuario.getIsDono()) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
        }
    }
}
