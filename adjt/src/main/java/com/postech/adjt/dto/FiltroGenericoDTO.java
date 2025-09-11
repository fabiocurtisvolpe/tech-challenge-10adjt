package com.postech.adjt.dto;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

public class FiltroGenericoDTO {

    @Schema(description = "Filtros aplicados à busca", example = "{\"nome:dono\": \"String\"}")
    private Map<String, String> filtros = new HashMap<>();

    @Schema(description = "Número da página", example = "0")
    private int pagina = 0;

    @Schema(description = "Tamanho da página", example = "10")
    private int tamanho = 10;

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
}
