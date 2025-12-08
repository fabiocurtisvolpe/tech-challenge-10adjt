package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.constants.TamanhoUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;

public class TipoUsuarioValidator {

    public static void validar(TipoUsuario tipoUsuario) throws NotificacaoException {
        if (tipoUsuario == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NULO_VALIDACAO);
        }

        if (tipoUsuario.getNome() == null || tipoUsuario.getNome().trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.NOME_EM_BRANCO);
        }

        if (tipoUsuario.getNome().length() > TamanhoUtil.NOME_MAXIMO_LENGTH) {
            throw new NotificacaoException(MensagemUtil.NOME_MAXIMO_CARACTERES);
        }

        if (tipoUsuario.getNome().length() < TamanhoUtil.NOME_MINIMO_LENGTH) {
            throw new NotificacaoException(MensagemUtil.NOME_MINIMO_CARACTERES);
        }

        if (tipoUsuario.getDescricao() != null && tipoUsuario.getDescricao().length() > TamanhoUtil.DESCRICAO_MAXIMA_LENGTH) {
            throw new NotificacaoException(MensagemUtil.DESCRICAO_MAXIMO_CARACTERES);
        }
    }
}
