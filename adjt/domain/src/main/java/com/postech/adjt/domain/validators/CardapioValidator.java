package com.postech.adjt.domain.validators;

import java.math.BigDecimal;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;

/**
 * Classe utilitária responsável pela validação de integridade e regras de negócio
 * da entidade Cardapio.
 *
 * Esta classe assegura que os dados obrigatórios estejam presentes e que o usuário
 * realizando a operação tenha a permissão necessária.
 */
public class CardapioValidator {

    /**
     * Realiza a validação completa de um objeto Cardapio.
     *
     * @param cardapio         A entidade Cardapio a ser validada.
     * @param idUsuarioLogado  O ID do usuário que está tentando realizar a operação.
     * @throws IllegalArgumentException Caso qualquer regra de negócio seja violada,
     *                                  com a mensagem correspondente definida em {@link MensagemUtil}.
     */
    public static void validar(Cardapio cardapio, Integer idUsuarioLogado) {

        // Validação de existência do objeto
        if (cardapio == null) {
            throw new IllegalArgumentException(MensagemUtil.CARDAPIO_NULO);
        }

        // Validação do nome: obrigatório e não pode ser apenas espaços
        if (cardapio.getNome() == null || cardapio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.NOME_CARDAPIO_OBRIGATORIO);
        }

        // Validação de vínculo com restaurante
        if (cardapio.getRestaurante() == null) {
            throw new IllegalArgumentException(MensagemUtil.RESTAURANTE_OBRIGATORIO);
        }

        // Validação de integridade do dono do restaurante
        if (cardapio.getRestaurante().getDono() == null) {
            throw new IllegalArgumentException(MensagemUtil.DONO_RESTAURANTE_CARDAPIO);
        }

        // Regra de Negócio: Apenas o dono do restaurante pode gerenciar o cardápio
        if (!cardapio.getRestaurante().getDono().getId().equals(idUsuarioLogado)) {
            throw new IllegalArgumentException(MensagemUtil.USUARIO_NAO_E_DONO_RESTAURANTE);
        }

        // Validação de preço: obrigatório e deve ser maior que zero
        if (cardapio.getPreco() == null || cardapio.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(MensagemUtil.PRECO_CARDAPIO_INVALIDO);
        }
    }
}