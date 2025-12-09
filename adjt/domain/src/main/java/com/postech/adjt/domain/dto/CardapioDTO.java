package com.postech.adjt.domain.dto;

import com.postech.adjt.domain.entidade.Restaurante;

public record CardapioDTO(Integer id,
    String nome,
    String descricao,
    Double preco,
    String foto,
    Boolean disponivel,
    Restaurante restaurante,
    Integer idUsuarioLogado) {
}
