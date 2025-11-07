package com.postech.adjt.domain.dto.filtro;

import jakarta.validation.constraints.NotBlank;

public class FiltroCampoDTO {

    @NotBlank(message = "O campo não pode estar em branco")
    private String campo;

    @NotBlank(message = "O operador não pode estar em branco")
    private String operador;

    @NotBlank(message = "O valor não pode estar em branco")
    private String valor;

    @NotBlank(message = "O tipo não pode estar em branco")
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
