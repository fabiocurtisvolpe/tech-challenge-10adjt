package com.postech.adjt.domain.model;

public class Endereco extends BaseModel {

    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String pontoReferencia;
    private String cep;
    private String municipio;
    private String uf;
    private Boolean principal = false;
    private Usuario usuario;

    public Endereco(Integer id, Boolean ativo, String logradouro,
            String numero, String complemento, String bairro,
            String pontoReferencia, String cep, String municipio, String uf,
            Boolean principal, Usuario usuario) {
        this.setId(id);
        this.setAtivo(ativo);
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.pontoReferencia = pontoReferencia;
        this.cep = cep;
        this.municipio = municipio;
        this.uf = uf;
        this.principal = principal;
        this.usuario = usuario;
    }

    public Endereco(Integer id, String logradouro,
            String numero, String complemento, String bairro,
            String pontoReferencia, String cep, String municipio, String uf,
            Boolean principal, Usuario usuario) {
        this.setId(id);
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.pontoReferencia = pontoReferencia;
        this.cep = cep;
        this.municipio = municipio;
        this.uf = uf;
        this.principal = principal;
        this.usuario = usuario;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public String getCep() {
        return cep;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getUf() {
        return uf;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
