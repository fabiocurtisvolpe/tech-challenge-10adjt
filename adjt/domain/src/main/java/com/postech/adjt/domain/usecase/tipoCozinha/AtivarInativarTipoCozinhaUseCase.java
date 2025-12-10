package com.postech.adjt.domain.usecase.tipoCozinha;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoCozinhaFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtivarInativarTipoCozinhaUseCase {

    private final GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;
    
    private AtivarInativarTipoCozinhaUseCase(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        this.tipoCozinhaRepository = tipoCozinhaRepository;
    }

    public static AtivarInativarTipoCozinhaUseCase create(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return new AtivarInativarTipoCozinhaUseCase(tipoCozinhaRepository);
    }

    public TipoCozinha run(Integer id, Boolean ativo) throws IllegalArgumentException {

        final TipoCozinha tipoCozinhaExistente = this.tipoCozinhaRepository.obterPorId(id).orElse(null);

        if (tipoCozinhaExistente == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_COZINHA_NAO_ENCONTRADO);
        }

        final TipoCozinha novoTipoCozinha = TipoCozinhaFactory.atualizar(tipoCozinhaExistente.getId(),
                tipoCozinhaExistente.getNome(), tipoCozinhaExistente.getDescricao(), ativo);
                
        return tipoCozinhaRepository.atualizar(novoTipoCozinha);
    }

}
