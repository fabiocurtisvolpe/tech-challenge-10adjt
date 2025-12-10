package com.postech.adjt.domain.usecase.tipoCozinha;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterTipoCozinhaPorIdUseCase {

    private final GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;
    
    private ObterTipoCozinhaPorIdUseCase(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        this.tipoCozinhaRepository = tipoCozinhaRepository;
    }

    public static ObterTipoCozinhaPorIdUseCase create(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return new ObterTipoCozinhaPorIdUseCase(tipoCozinhaRepository);
    }

    public Optional<TipoCozinha> run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }
        
        Optional<TipoCozinha> tipoCozinhaExistente = this.tipoCozinhaRepository.obterPorId(id);
        
        if (tipoCozinhaExistente.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.TIPO_COZINHA_NAO_ENCONTRADO);
        }

        return tipoCozinhaExistente;
    }
 
}
