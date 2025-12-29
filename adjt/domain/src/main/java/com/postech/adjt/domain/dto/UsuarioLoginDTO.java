package com.postech.adjt.domain.dto;

import com.postech.adjt.domain.entidade.TipoUsuario;

import lombok.Getter;

@Getter
public class UsuarioLoginDTO {

    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;

    public UsuarioLoginDTO(String nome, String email, String senha, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }
}
