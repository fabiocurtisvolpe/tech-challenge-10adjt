package com.postech.adjt.domain.model.filtro;

import java.util.ArrayList;
import java.util.List;

public class FiltroGenerico {

    private List<FiltroCampo> filtros = new ArrayList<>();
    private int pagina = 0;
    private int tamanho = 10;

    public List<FiltroCampo> getFiltros() {
        return filtros;
    }

    public void setFiltros(List<FiltroCampo> filtros) {
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
