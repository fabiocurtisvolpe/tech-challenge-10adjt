package com.postech.adjt.domain.dto;

public record TipoUsuarioDTO(Integer id, String nome, String descricao, Boolean ativo, Boolean isDono,
        RestauranteDTO restaurante) {

}
