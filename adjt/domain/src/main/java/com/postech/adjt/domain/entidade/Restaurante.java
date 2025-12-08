package com.postech.adjt.domain.entidade;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Restaurante extends Base {

    protected String nome;
    protected String descricao;
    protected String horarioFuncionamento;
    protected TipoCozinha tipoCozinha;
    protected Endereco endereco;
    protected Usuario dono;
}
