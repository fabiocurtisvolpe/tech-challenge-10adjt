package com.postech.adjt.domain.dto.filtro;

public class FiltroCampoDTO {

    private String campo;
    private String operador;
    private String valor;
    private String tipo;

    public FiltroCampoDTO() {
    }

    public FiltroCampoDTO(String campo, String operador, String valor, String tipo) {
        this.campo = campo;
        this.operador = operador;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
