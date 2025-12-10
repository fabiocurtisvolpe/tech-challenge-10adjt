package com.postech.adjt.domain.usecase.cardapio;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterCardapioPorIdUseCase {

    private final GenericRepositoryPort<Cardapio> cardapioRepository;

    private ObterCardapioPorIdUseCase(GenericRepositoryPort<Cardapio> cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    public static ObterCardapioPorIdUseCase create(GenericRepositoryPort<Cardapio> cardapioRepository) {
        return new ObterCardapioPorIdUseCase(cardapioRepository);
    }

    public Optional<Cardapio> run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }
        
        Optional<Cardapio> cardapioExistente = this.cardapioRepository.obterPorId(id);
        
        if (cardapioExistente.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO);
        }

        return cardapioExistente;
    }
 
}
