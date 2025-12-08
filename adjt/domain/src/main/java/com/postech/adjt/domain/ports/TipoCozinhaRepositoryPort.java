package com.postech.adjt.domain.ports;

import java.util.List;
import java.util.Optional;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;

public interface TipoCozinhaRepositoryPort {

    TipoCozinha criar(TipoCozinha tipoCozinha);

    Optional<TipoCozinha> obterPorId(Integer id);

    TipoCozinha atualizar(TipoCozinha tipoCozinha);

    ResultadoPaginacaoDTO<TipoCozinha> listarPaginado(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts);

    Boolean ativarDesativar(TipoCozinha tipoCozinha);

}
