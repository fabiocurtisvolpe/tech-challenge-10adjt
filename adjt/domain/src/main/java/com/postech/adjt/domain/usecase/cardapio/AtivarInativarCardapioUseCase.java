package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.CardapioRepositoryPort;
import com.postech.adjt.domain.validators.CardapioValidator;

public class AtivarInativarCardapioUseCase {

    private final CardapioRepositoryPort cardapioRepository;

    private AtivarInativarCardapioUseCase(CardapioRepositoryPort cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    public static AtivarInativarCardapioUseCase create(CardapioRepositoryPort cardapioRepository) {
        return new AtivarInativarCardapioUseCase(cardapioRepository);
    }

    public Cardapio run(Integer id, Boolean ativo, Integer idUsuarioLogado) throws IllegalArgumentException {

        final Cardapio cardapioExistente = this.cardapioRepository.obterPorId(id).orElse(null);

        if (cardapioExistente == null) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO);
        }

        final Cardapio cardapio = CardapioFactory.atualizar(id, cardapioExistente.getNome(),
                cardapioExistente.getDescricao(), cardapioExistente.getPreco(), cardapioExistente.getFoto(),
                cardapioExistente.getDisponivel(), cardapioExistente.getRestaurante(), ativo);

        CardapioValidator.validar(cardapio, idUsuarioLogado);

        return cardapioRepository.atualizar(cardapio);
    }

}
