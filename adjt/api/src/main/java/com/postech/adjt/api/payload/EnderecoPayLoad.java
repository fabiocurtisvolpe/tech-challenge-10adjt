package com.postech.adjt.api.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EnderecoPayLoad {

@NotBlank(message = "Logradouro não pode estar em branco")
    private String logradouro;

    @Size(max = 5, message = "Número deve ter até 5 caracteres")
    private String numero;

    private String complemento;

    @NotBlank(message = "Bairro não pode estar em branco")
    private String bairro;

    private String pontoReferencia;

    @NotBlank(message = "CEP não pode estar em branco")
    private String cep;

    @NotBlank(message = "Município não pode estar em branco")
    private String municipio;

    @NotBlank(message = "UF não pode estar em branco")
    @Size(min = 2, max = 2, message = "UF deve ter 2 caracteres")
    private String uf;

    private Boolean principal = false;

    public EnderecoPayLoad() {
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }
}
