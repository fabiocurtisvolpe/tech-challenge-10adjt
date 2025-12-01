package com.postech.adjt.api.dto;

/**
 * DTO de resposta para Endereco que oculta informações sensíveis
 * 
 * Não inclui: id, dataCriacao, ativo, usuario
 * 
 * @author Fabio
 * @since 2025-12-01
 */
public class EnderecoRespostaDTO {

    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String pontoReferencia;
    private String cep;
    private String municipio;
    private String uf;
    private Boolean principal;

    public EnderecoRespostaDTO(String logradouro, String numero, String complemento, String bairro,
            String pontoReferencia, String cep, String municipio, String uf, Boolean principal) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.pontoReferencia = pontoReferencia;
        this.cep = cep;
        this.municipio = municipio;
        this.uf = uf;
        this.principal = principal;
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
