package com.postech.adjt.domain.usecase.tipoCozinha;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoCozinhaDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoCozinhaFactory;
import com.postech.adjt.domain.ports.TipoCozinhaRepositoryPort;
import com.postech.adjt.domain.validators.TipoCozinhaValidator;

public class AtualizarTipoCozinhaUseCase {

    private final TipoCozinhaRepositoryPort tipoCozinhaRepository;


    private AtualizarTipoCozinhaUseCase(TipoCozinhaRepositoryPort tipoCozinhaRepository) {
        this.tipoCozinhaRepository = tipoCozinhaRepository;
    }

    public static AtualizarTipoCozinhaUseCase create(TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort) {
        return new AtualizarTipoCozinhaUseCase(tipoCozinhaRepositoryPort);
    }

    public TipoCozinha run(TipoCozinhaDTO dto) {
        
        final TipoCozinha TipoCozinhaExistente = this.tipoCozinhaRepository.obterPorId(dto.id()).orElse(null);
        
        if (TipoCozinhaExistente == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_COZINHA_NAO_ENCONTRADO);
        }

        final TipoCozinha tipoCozinha = TipoCozinhaFactory.atualizar(TipoCozinhaExistente.getId(), 
                dto.nome(), dto.descricao(), true);

        TipoCozinhaValidator.validar(tipoCozinha);

        return tipoCozinhaRepository.atualizar(tipoCozinha);
    }

}
