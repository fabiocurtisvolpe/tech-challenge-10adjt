package com.postech.adjt.domain.ports;

import java.util.List;
import java.util.Optional;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Cardapio;

public interface CardapioRepositoryPort {

    Cardapio criar(Cardapio cardapio);

    Optional<Cardapio> obterPorId(Integer id);
    Optional<Cardapio> obterPorNome(String nome);

    Cardapio atualizar(Cardapio cardapio);

    ResultadoPaginacaoDTO<Cardapio> listarPaginado(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts);

    Boolean ativarDesativar(Cardapio cardapio);
}
