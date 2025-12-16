package com.postech.adjt.domain.validators;

import java.math.BigDecimal;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;

public class CardapioValidator {

    public static void validar(Cardapio cardapio, Integer idUsuarioLogado) {
        
        if (cardapio == null) {
            throw new IllegalArgumentException(MensagemUtil.CARDAPIO_NULO);
        }

        if (cardapio.getNome() == null || cardapio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.NOME_CARDAPIO_OBRIGATORIO);
        }

        if (cardapio.getRestaurante() == null) {
            throw new IllegalArgumentException(MensagemUtil.RESTAURANTE_OBRIGATORIO);
        }

        if (cardapio.getRestaurante().getDono() == null) {
            throw new IllegalArgumentException(MensagemUtil.DONO_RESTAURANTE_CARDAPIO);
        }

        if (!cardapio.getRestaurante().getDono().getId().equals(idUsuarioLogado)) {
            throw new IllegalArgumentException(MensagemUtil.USUARIO_NAO_E_DONO_RESTAURANTE);
        }

        if (cardapio.getPreco() == null || cardapio.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(MensagemUtil.PRECO_CARDAPIO_INVALIDO);
        } 
    }
}
