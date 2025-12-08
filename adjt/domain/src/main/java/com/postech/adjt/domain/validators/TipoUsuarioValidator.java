package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;

public class TipoUsuarioValidator {

    private static final int NOME_MAXIMO_LENGTH = 50;
    private static final int NOME_DESCRICAO_LENGTH = 200;

    public static void validar(TipoUsuario tipoUsuario) throws NotificacaoException {
        if (tipoUsuario == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NULO_VALIDACAO);
        }

        if (tipoUsuario.getNome() == null || tipoUsuario.getNome().trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.NOME_EM_BRANCO);
        }

        if (tipoUsuario.getNome().length() > NOME_MAXIMO_LENGTH) {
            throw new NotificacaoException(MensagemUtil.NOME_MAXIMO_CARACTERES);
        }

        if (tipoUsuario.getDescricao() != null && tipoUsuario.getDescricao().length() > NOME_DESCRICAO_LENGTH) {
            throw new NotificacaoException(MensagemUtil.DESCRICAO_MAXIMO_CARACTERES);
        }
    }
}
