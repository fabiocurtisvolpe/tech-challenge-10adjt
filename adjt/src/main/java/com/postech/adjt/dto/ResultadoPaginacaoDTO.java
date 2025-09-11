package com.postech.adjt.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class ResultadoPaginacaoDTO<T> implements Serializable {

    private static final long serialVersionUID = 907682988992882263L;

    @Schema(description = "Lista de resultados da página")
    private final List<?> resultado;

    @Schema(description = "Quantidade total de registros encontrados")
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
