package com.postech.adjt.domain.dto.filtro;

import java.util.ArrayList;
import java.util.List;

public class FiltroGenericoDTO {

    private List<FiltroCampoDTO> filtros = new ArrayList<>();
    private int pagina = 0;
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
