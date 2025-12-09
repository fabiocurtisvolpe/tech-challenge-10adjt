package com.postech.adjt.domain.dto;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.Usuario;

public record RestauranteDTO(Integer id, 
    String nome,
    String descricao,
    String horarioFuncionamento,
    TipoCozinha tipoCozinha,
    Endereco endereco,
    Usuario dono) {

}
