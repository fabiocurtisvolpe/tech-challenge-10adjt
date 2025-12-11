package com.postech.adjt.domain.ports;

import java.util.List;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;


public interface GenericPaginadoRepositoryPort<T> {
    ResultadoPaginacaoDTO<T> listarPaginado(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts);
}
