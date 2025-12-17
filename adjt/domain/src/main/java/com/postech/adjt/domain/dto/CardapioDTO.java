package com.postech.adjt.domain.dto;

import java.math.BigDecimal;

public record CardapioDTO(Integer id,
    String nome,
    String descricao,
    BigDecimal preco,
    String foto,
    Boolean disponivel,
    RestauranteDTO restaurante) {
}
