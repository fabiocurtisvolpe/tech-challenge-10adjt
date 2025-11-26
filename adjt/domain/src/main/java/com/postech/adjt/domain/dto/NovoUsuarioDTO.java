package com.postech.adjt.domain.dto;

import java.util.List;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;

public record NovoUsuarioDTO(String nome, String email, String senha, TipoUsuarioEnum tipoUsuario, List<Endereco> enderecos) {
}
