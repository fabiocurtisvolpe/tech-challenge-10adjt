package com.postech.adjt.domain.dto;

import java.io.Serializable;
import java.util.List;

public class ResultadoPaginacaoDTO<T> implements Serializable {

    private static final long serialVersionUID = 907682988992882263L;

    private final List<?> resultado;
    private final int quantidade;

    public ResultadoPaginacaoDTO(List<?> resultado, int quantidade) {
        this.resultado = resultado;
        this.quantidade = quantidade;
    }

    public List<?> getResultado() {
        return resultado;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
