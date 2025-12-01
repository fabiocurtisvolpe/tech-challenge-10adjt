package com.postech.adjt.domain.entidade;

import org.apache.commons.validator.routines.RegexValidator;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Endereco extends Base {

    private static final RegexValidator CEP_VALIDATOR = new RegexValidator("^[0-9]{5}-?[0-9]{3}$");

    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String pontoReferencia;
    private String cep;
    private String municipio;
    private String uf;
    private Boolean principal = false;

    @JsonIgnore
    private Usuario usuario;

    public Endereco(String logradouro, String numero, String complemento, String bairro, String pontoReferencia,
            String cep, String municipio, String uf, Boolean principal, Usuario usuario) throws IllegalArgumentException {

        this.validarCep(cep);

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

    public Endereco(Integer id, String logradouro, String numero, String complemento, String bairro, String pontoReferencia,
            String cep, String municipio, String uf, Boolean principal, Boolean ativo, Usuario usuario) throws IllegalArgumentException {

        this.validarCep(cep);

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

    private void validarCep(String cep) {
        if (cep == null || !CEP_VALIDATOR.isValid(cep)) {
            throw new IllegalArgumentException("CEP inválido");
        }
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
