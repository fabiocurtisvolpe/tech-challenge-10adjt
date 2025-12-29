package com.postech.adjt.domain.entidade;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Cardapio extends Base {

    protected String nome;
    protected String descricao;
    protected BigDecimal preco;
    protected String foto;
    protected Boolean disponivel;

    @JsonIgnore
    protected Restaurante restaurante;
}
