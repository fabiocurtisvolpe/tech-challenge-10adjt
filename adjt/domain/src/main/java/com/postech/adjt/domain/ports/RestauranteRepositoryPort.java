package com.postech.adjt.domain.ports;

import java.util.List;
import java.util.Optional;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Restaurante;

public interface RestauranteRepositoryPort {

    Restaurante criar(Restaurante restaurante);

    Optional<Restaurante> obterPorId(Integer id);

    Restaurante atualizar(Restaurante restaurante);

    ResultadoPaginacaoDTO<Restaurante> listarPaginado(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts);

    Boolean ativarDesativar(Restaurante restaurante);

}
