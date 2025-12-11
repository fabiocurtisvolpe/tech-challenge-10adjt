package com.postech.adjt.domain.usecase;

import java.util.List;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class PaginadoUseCase<T> {

    private final GenericRepositoryPort<T> repository;

    private PaginadoUseCase(GenericRepositoryPort<T> repository) {
        this.repository = repository;
    }

    public static <T> PaginadoUseCase<T> create(GenericRepositoryPort<T> repository) {
        return new PaginadoUseCase<>(repository);
    }

    public ResultadoPaginacaoDTO<T> run(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts) {
        
        if (page < 0 || size <= 0) {
            throw new NotificacaoException(MensagemUtil.PAGINA_SIZE_INVALIDA);
        }

        return repository.listarPaginado(page, size, filters, sorts);
    }
}

