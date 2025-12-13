package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.constants.TamanhoUtil;
import com.postech.adjt.domain.entidade.Restaurante;

public class RestauranteValidator {

    public static void validar(Restaurante restaurante, Integer idUsuarioLogado) {
        
        if (restaurante == null) {
            throw new IllegalArgumentException(MensagemUtil.RESTAURANTE_NULO);
        }

        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.NOME_RESTAURANTE_OBRIGATORIO);
        }

        if (restaurante.getNome().length() < TamanhoUtil.NOME_MINIMO_LENGTH) {
            throw new IllegalArgumentException(MensagemUtil.NOME_MINIMO_CARACTERES);
        }

        if (restaurante.getNome().length() > TamanhoUtil.NOME_MAXIMO_LENGTH) {
            throw new IllegalArgumentException(MensagemUtil.NOME_MAXIMO_CARACTERES);
        }

        if (restaurante.getHorarioFuncionamento() == null || restaurante.getHorarioFuncionamento().trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.HORARIO_FUNCIONAMENTO_OBRIGATORIO);
        }

        if (restaurante.getTipoCozinha() == null) {
            throw new IllegalArgumentException(MensagemUtil.TIPO_COZINHA_OBRIGATORIO);
        }

        if (restaurante.getDono() == null) {
            throw new IllegalArgumentException(MensagemUtil.DONO_RESTAURANTE_OBRIGATORIO);
        }

        if (!restaurante.getDono().getId().equals(idUsuarioLogado)) {
            throw new IllegalArgumentException(MensagemUtil.USUARIO_NAO_E_DONO_RESTAURANTE);
        }

        if (restaurante.getDescricao() != null && restaurante.getDescricao().length() > TamanhoUtil.DESCRICAO_MAXIMA_LENGTH) {
            throw new IllegalArgumentException(MensagemUtil.DESCRICAO_MAXIMO_CARACTERES);
        }

        HorarioRestauranteValidator.validar(restaurante.getHorarioFuncionamento());
    }
}
