package com.postech.adjt.domain.dto;

import java.util.List;

import com.postech.adjt.domain.entidade.Endereco;

public record AtualizaUsuarioDTO(String nome, String email, List<Endereco> enderecos) {
}
