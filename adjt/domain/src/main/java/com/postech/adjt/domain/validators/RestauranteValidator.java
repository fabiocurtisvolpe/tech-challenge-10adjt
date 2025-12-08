package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Restaurante;

public class RestauranteValidator {

    public static void validar(Restaurante restaurante) {
        
        if (restaurante == null) {
            throw new IllegalArgumentException(MensagemUtil.RESTAURANTE_NULO);
        }

        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.NOME_RESTAURANTE_OBRIGATORIO);
        }

        if (restaurante.getHorarioFuncionamento() == null || restaurante.getHorarioFuncionamento().trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.HORARIO_FUNCIONAMENTO_OBRIGATORIO);
        }

        if (restaurante.getTipoCozinha() == null) {
            throw new IllegalArgumentException(MensagemUtil.TIPO_COZINHA_OBRIGATORIO);
        }

        if (restaurante.getEndereco() == null) {
            throw new IllegalArgumentException(MensagemUtil.ENDERECO_OBRIGATORIO);
        }

        if (restaurante.getDono() == null) {
            throw new IllegalArgumentException(MensagemUtil.DONO_RESTAURANTE_OBRIGATORIO);
        }
    }

}
