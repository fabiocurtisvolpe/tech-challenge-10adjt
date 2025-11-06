package com.postech.adjt.domain.service;

import java.util.List;

import com.postech.adjt.domain.model.ResultadoPaginacao;
import com.postech.adjt.domain.model.filtro.FiltroGenerico;

public interface BaseService<T> {

    T criar(T entidade);

    T atualizar(Integer id, T entidade);

    T buscar(Integer id);

    List<T> listar();

    ResultadoPaginacao<T> listarPaginado(FiltroGenerico filtro);

    boolean ativarInativar(Integer id);
}
