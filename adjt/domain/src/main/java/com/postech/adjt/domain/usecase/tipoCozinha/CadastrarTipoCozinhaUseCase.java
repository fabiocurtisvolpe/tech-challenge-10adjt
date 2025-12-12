package com.postech.adjt.domain.usecase.tipoCozinha;

import com.postech.adjt.domain.dto.TipoCozinhaDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.factory.TipoCozinhaFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.validators.TipoCozinhaValidator;

public class CadastrarTipoCozinhaUseCase {

    private final GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;

    private CadastrarTipoCozinhaUseCase(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        this.tipoCozinhaRepository = tipoCozinhaRepository;
    }

    public static CadastrarTipoCozinhaUseCase create(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return new CadastrarTipoCozinhaUseCase(tipoCozinhaRepository);
    }

    public TipoCozinha run(TipoCozinhaDTO dto) {
        
        final TipoCozinha tipoCozinha = TipoCozinhaFactory.novo(dto.nome(), dto.descricao());
        
        TipoCozinhaValidator.validar(tipoCozinha);

        return tipoCozinhaRepository.criar(tipoCozinha);
    }
}
