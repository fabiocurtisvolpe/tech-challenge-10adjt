package com.postech.adjt.dto.filtro;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class FiltroGenericoDTO {

    @Schema(description = "Filtros aplicados à busca", example = "{\"campo\": \"nomeCampo\", \"operador\": \"like\", \"valor\": \"valorDesejado\", \"tipo\": \"string\"   }")
    private List<FiltroCampoDTO> filtros = new ArrayList<>();

    @Schema(description = "Número da página", example = "0")
    private int pagina = 0;

    @Schema(description = "Tamanho da página", example = "10")
    private int tamanho = 10;

    public List<FiltroCampoDTO> getFiltros() {
        return filtros;
    }

    public void setFiltros(List<FiltroCampoDTO> filtros) {
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
