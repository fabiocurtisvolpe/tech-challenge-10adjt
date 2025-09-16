package com.postech.adjt.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioSenhaDTO {

    @NotNull(message = "O ID não pode ser nulo")
    private Integer id;

    @NotBlank(message = "A senha não pode estar em branco")
    @NotNull(message = "A senha não pode ser nula")
    @Size(max = 50, message = "A senha deve ter até 50 caracteres")
    private String senha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
