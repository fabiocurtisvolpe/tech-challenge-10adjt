package com.postech.adjt.domain.dto;

import java.util.List;

import com.postech.adjt.domain.entidade.Endereco;

public record NovoUsuarioDTO(String nome, String email, String senha, TipoUsuarioDTO tipoUsuario, List<Endereco> enderecos) {
}
