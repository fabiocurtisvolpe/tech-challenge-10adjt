package com.postech.adjt.dto;

import java.util.HashMap;
import java.util.Map;

public class FiltroGenericoDTO {
    private Map<String, String> filtros = new HashMap<>();
    private int pagina = 0;
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
