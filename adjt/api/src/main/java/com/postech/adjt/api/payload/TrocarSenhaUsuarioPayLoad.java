package com.postech.adjt.api.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TrocarSenhaUsuarioPayLoad {

    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "O e-mail no formato inválido")
    @Size(max = 50, message = "O e-mail deve ter até 50 caracteres")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(max = 50, message = "A senha deve ter até 50 caracteres")
    private String senha;

    public TrocarSenhaUsuarioPayLoad() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
