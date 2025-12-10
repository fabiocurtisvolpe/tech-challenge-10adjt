package com.postech.adjt.domain.ports;

import java.util.List;
import java.util.Optional;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;


public interface GenericRepositoryPort<T> {

    T criar(T entidade);
    T atualizar(T entidade);

    Optional<T> obterPorId(Integer id);
    Optional<T> obterPorNome(String nome);
    Optional<T> obterPorEmail(String email);

    ResultadoPaginacaoDTO<T> listarPaginado(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts);

    Boolean ativarDesativar(T entidade);
}
