package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.CardapioRepositoryPort;
import com.postech.adjt.domain.validators.CardapioValidator;

public class CadastrarCardapioUseCase {

    private final CardapioRepositoryPort cardapioRepository;

    private CadastrarCardapioUseCase(CardapioRepositoryPort cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    public static CadastrarCardapioUseCase create(CardapioRepositoryPort cardapioRepository) {
        return new CadastrarCardapioUseCase(cardapioRepository);
    }

    public Cardapio run(CardapioDTO dto) {

        final Cardapio cardapioExistente = this.cardapioRepository.obterPorId(dto.id()).orElse(null);

        if (cardapioExistente != null) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_JA_CADASTRADO);
        }

        final Cardapio cardapio = CardapioFactory.criar(dto.nome(), dto.descricao(),
                dto.preco(),
                dto.foto(), dto.disponivel(), dto.restaurante());

        CardapioValidator.validar(cardapio, dto.idUsuarioLogado());

        return cardapioRepository.criar(cardapio);
    }

}
