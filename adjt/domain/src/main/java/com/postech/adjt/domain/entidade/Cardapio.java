package com.postech.adjt.domain.entidade;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Cardapio extends Base {

    protected String nome;
    protected String descricao;
    protected Double preco;
    protected String foto;
    protected Boolean disponivel;
    protected Restaurante restaurante;
}
