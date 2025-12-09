package com.postech.adjt.domain.usecase.tipoCozinha;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoCozinhaFactory;
import com.postech.adjt.domain.ports.TipoCozinhaRepositoryPort;
import com.postech.adjt.domain.validators.TipoCozinhaValidator;

public class CadastrarTipoCozinhaUseCase {

    private final TipoCozinhaRepositoryPort tipoCozinhaRepository;

    private CadastrarTipoCozinhaUseCase(TipoCozinhaRepositoryPort tipoCozinhaRepository) {
        this.tipoCozinhaRepository = tipoCozinhaRepository;
    }

    public static CadastrarTipoCozinhaUseCase create(TipoCozinhaRepositoryPort tipoCozinhaRepository) {
        return new CadastrarTipoCozinhaUseCase(tipoCozinhaRepository);
    }

    public TipoCozinha run(TipoCozinha dto) {
        

        final TipoCozinha tipoCozinhaExistente = this.tipoCozinhaRepository.obterPorId(dto.getId()).orElse(null);
        
        if (tipoCozinhaExistente != null) {
            throw new NotificacaoException(MensagemUtil.TIPO_COZINHA_NAO_ENCONTRADO);
        }

        final TipoCozinha tipoCozinha = TipoCozinhaFactory.criar(dto.getNome(), dto.getDescricao());
        
        TipoCozinhaValidator.validar(tipoCozinha);

        return tipoCozinhaRepository.criar(tipoCozinha);
    }
 
}
