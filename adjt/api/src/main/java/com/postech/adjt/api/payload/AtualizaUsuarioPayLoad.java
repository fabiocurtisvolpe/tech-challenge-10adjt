package com.postech.adjt.api.payload;

import java.util.List;

import com.postech.adjt.domain.entidade.Endereco;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AtualizaUsuarioPayLoad {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 50, message = "O nome deve ter até 50 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "O e-mail no formato inválido")
    @Size(max = 50, message = "O e-mail deve ter até 50 caracteres")
    private String email;

    @NotNull(message = "O endereço não pode ser nulo")
    @Size(min = 1, message = "O endereço deve conter pelo menos um item")
    private List<Endereco> enderecos;

    public AtualizaUsuarioPayLoad() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }
}
