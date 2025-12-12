package com.postech.adjt.domain.dto;

import com.postech.adjt.domain.enums.TipoCozinhaEnum;

public record RestauranteDTO(Integer id, 
    String nome,
    String descricao,
    String horarioFuncionamento,
    TipoCozinhaEnum tipoCozinha,
    EnderecoDTO endereco,
    UsuarioDTO dono) {
}
