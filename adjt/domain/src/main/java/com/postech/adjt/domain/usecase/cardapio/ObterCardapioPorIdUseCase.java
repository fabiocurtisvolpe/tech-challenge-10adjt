package com.postech.adjt.domain.usecase.cardapio;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterCardapioPorIdUseCase {

    private final GenericRepositoryPort<Cardapio> repositoryPort;

    private ObterCardapioPorIdUseCase(GenericRepositoryPort<Cardapio> repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public static ObterCardapioPorIdUseCase create(GenericRepositoryPort<Cardapio> repositoryPort) {
        return new ObterCardapioPorIdUseCase(repositoryPort);
    }

    public Optional<Cardapio> run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }
        
        Optional<Cardapio> cardapioExistente = this.repositoryPort.obterPorId(id);
        
        if (cardapioExistente.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO);
        }

        return cardapioExistente;
    }
 
}
