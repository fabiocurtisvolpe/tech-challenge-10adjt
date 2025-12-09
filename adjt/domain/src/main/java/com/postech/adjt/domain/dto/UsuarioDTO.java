package com.postech.adjt.domain.dto;

import java.util.List;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;

public record UsuarioDTO(String nome, String email, String senha, 
    TipoUsuario tipoUsuario, List<Endereco> enderecos) {
}
