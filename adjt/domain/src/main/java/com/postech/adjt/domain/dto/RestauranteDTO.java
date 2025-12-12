package com.postech.adjt.domain.dto;

public record RestauranteDTO(Integer id, 
    String nome,
    String descricao,
    String horarioFuncionamento,
    TipoCozinhaDTO tipoCozinha,
    EnderecoDTO endereco,
    UsuarioDTO dono) {
}
