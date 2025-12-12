package com.postech.adjt.domain.dto;

import java.util.List;

public record UsuarioDTO(Integer id, String nome, String email, String senha,
        TipoUsuarioDTO tipoUsuario, List<EnderecoDTO> enderecos, Boolean ativo) {
}
