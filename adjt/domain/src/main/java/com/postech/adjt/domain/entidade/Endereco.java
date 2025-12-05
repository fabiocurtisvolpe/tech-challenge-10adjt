package com.postech.adjt.domain.entidade;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Endereco extends Base {

    protected String logradouro;
    protected String numero;
    protected String complemento;
    protected String bairro;
    protected String pontoReferencia;
    protected String cep;
    protected String municipio;
    protected String uf;
    protected Boolean principal;

    @JsonIgnore
    protected Usuario usuario;
}
