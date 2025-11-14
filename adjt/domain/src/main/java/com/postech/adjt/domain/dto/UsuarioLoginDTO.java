package com.postech.adjt.domain.dto;

import com.postech.adjt.domain.enums.TipoUsuarioEnum;

public class UsuarioLoginDTO {

    private String nome;
    private String email;
    private String senha;
    private TipoUsuarioEnum tipoUsuario;

    public UsuarioLoginDTO(String nome, String email, String senha, TipoUsuarioEnum tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public TipoUsuarioEnum getTipoUsuario() {
        return tipoUsuario;
    }

}
