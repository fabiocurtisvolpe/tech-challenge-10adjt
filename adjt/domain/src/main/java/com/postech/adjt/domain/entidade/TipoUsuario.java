package com.postech.adjt.domain.entidade;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class TipoUsuario extends Base {

    protected String nome;
    protected String descricao;
    protected Boolean isEditavel;

}
