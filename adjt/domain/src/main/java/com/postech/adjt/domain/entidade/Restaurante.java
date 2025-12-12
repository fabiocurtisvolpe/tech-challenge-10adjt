package com.postech.adjt.domain.entidade;

import com.postech.adjt.domain.enums.TipoCozinhaEnum;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Restaurante extends Base {

    protected String nome;
    protected String descricao;
    protected String horarioFuncionamento;
    protected TipoCozinhaEnum tipoCozinha;
    protected Endereco endereco;
    protected Usuario dono;
}
