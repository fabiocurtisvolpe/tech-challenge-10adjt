package com.postech.adjt.domain.entidade;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class TipoCozinha extends Base {

    protected String nome;
    protected String descricao;
}
