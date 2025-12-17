package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterCardapioPorIdUseCase {

    private final GenericRepositoryPort<Cardapio> cardapioRepositoryPort;

    private ObterCardapioPorIdUseCase(GenericRepositoryPort<Cardapio> cardapioRepositoryPort) {
        this.cardapioRepositoryPort = cardapioRepositoryPort;
    }

    public static ObterCardapioPorIdUseCase create(GenericRepositoryPort<Cardapio> cardapioRepositoryPort) {
        return new ObterCardapioPorIdUseCase(cardapioRepositoryPort);
    }

    public Cardapio run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        return this.cardapioRepositoryPort.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO));
    }
}
