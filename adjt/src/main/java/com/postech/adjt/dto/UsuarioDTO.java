package com.postech.adjt.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDTO extends BaseDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 50, message = "O nome deve ter até 50 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "O e-mail no formato inválido")
    @Size(max = 50, message = "O e-mail deve ter até 50 caracteres")
    private String email;

    @NotBlank(message = "O login não pode estar em branco")
    @Size(max = 50, message = "O login deve ter até 50 caracteres")
    private String login;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(max = 50, message = "A senha deve ter até 50 caracteres")
    private String senha;

    @NotNull(message = "O tipo do usuári não pode ser nulo")
    private TipoUsuarioDTO tipoUsuario;

    @NotNull(message = "O endereço não pode ser nulo")
    @Size(min = 1, message = "O endereço deve conter pelo menos um item")
    private List<EnderecoDTO> enderecos = new ArrayList<>();

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuarioDTO getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuarioDTO tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<EnderecoDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoDTO> enderecos) {
        this.enderecos = enderecos;
    }
}
