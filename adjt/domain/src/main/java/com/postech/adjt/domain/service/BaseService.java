package com.postech.adjt.domain.service;

import java.util.List;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FiltroGenericoDTO;

public interface BaseService<T> {

    T criar(T entidade);

    T atualizar(Integer id, T entidade);

    T buscar(Integer id);

    List<T> listar();

    ResultadoPaginacaoDTO<T> listarPaginado(FiltroGenericoDTO filtro);

    boolean ativarInativar(Integer id);
}
