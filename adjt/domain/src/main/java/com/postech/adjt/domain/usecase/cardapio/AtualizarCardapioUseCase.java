package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.CardapioRepositoryPort;
import com.postech.adjt.domain.validators.CardapioValidator;

public class AtualizarCardapioUseCase {

    private final CardapioRepositoryPort cardapioRepository;


    private AtualizarCardapioUseCase(CardapioRepositoryPort cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    public static AtualizarCardapioUseCase create(CardapioRepositoryPort cardapioRepository) {
        return new AtualizarCardapioUseCase(cardapioRepository);
    }

    public Cardapio run(CardapioDTO dto) {
        
        final Cardapio cardapioExistente = this.cardapioRepository.obterPorId(dto.id()).orElse(null);
        
        if (cardapioExistente == null) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO);
        }

        final Cardapio cardapio = CardapioFactory.atualizar(cardapioExistente.getId(),
                dto.nome(), dto.descricao(), dto.preco(),
                dto.foto(), dto.disponivel(), dto.restaurante(),
                true);

        CardapioValidator.validar(cardapio, dto.idUsuarioLogado());

        return cardapioRepository.atualizar(cardapio);
    }

}
