package com.postech.adjt.domain.ports;

import java.util.List;
import java.util.Optional;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;

public interface TipoUsuarioRepositoryPort {

    TipoUsuario criar(TipoUsuario tipoUsuario);

    Optional<TipoUsuario> obterPorId(Integer id);

    TipoUsuario atualizar(TipoUsuario tipoUsuario);

    ResultadoPaginacaoDTO<TipoUsuario> listarPaginado(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts);

    Boolean ativarDesativar(TipoUsuario tipoUsuario);
}
