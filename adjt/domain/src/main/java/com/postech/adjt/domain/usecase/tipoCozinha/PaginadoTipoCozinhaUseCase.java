package com.postech.adjt.domain.usecase.tipoCozinha;

import java.util.List;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class PaginadoTipoCozinhaUseCase {

    private final GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;

    private PaginadoTipoCozinhaUseCase(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        this.tipoCozinhaRepository = tipoCozinhaRepository;
    }

    public static PaginadoTipoCozinhaUseCase create(GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return new PaginadoTipoCozinhaUseCase(tipoCozinhaRepository);
    }

    public ResultadoPaginacaoDTO<TipoCozinha> run(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts) {
        
        if (page < 0 || size <= 0) {
            throw new NotificacaoException(MensagemUtil.PAGINA_SIZE_INVALIDA);
        }

        return tipoCozinhaRepository.listarPaginado(page, size, filters, sorts);
    }
 
}
