package com.postech.adjt.domain.usecase.tipoCozinha;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoCozinhaDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoCozinhaFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.validators.TipoCozinhaValidator;

public class AtualizarTipoCozinhaUseCase {

    private final GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;

    private AtualizarTipoCozinhaUseCase(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        this.tipoCozinhaRepository = tipoCozinhaRepository;
    }

    public static AtualizarTipoCozinhaUseCase create(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return new AtualizarTipoCozinhaUseCase(tipoCozinhaRepository);
    }

    public TipoCozinha run(TipoCozinhaDTO dto) {
        
        final TipoCozinha TipoCozinhaExistente = this.tipoCozinhaRepository.obterPorId(dto.id()).orElse(null);
        
        if (TipoCozinhaExistente == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_COZINHA_NAO_ENCONTRADO);
        }

        final TipoCozinha tipoCozinha = TipoCozinhaFactory.tipoCozinha(TipoCozinhaExistente.getId(), 
                dto.nome(), dto.descricao(), true);

        TipoCozinhaValidator.validar(tipoCozinha);

        return tipoCozinhaRepository.atualizar(tipoCozinha);
    }
}
