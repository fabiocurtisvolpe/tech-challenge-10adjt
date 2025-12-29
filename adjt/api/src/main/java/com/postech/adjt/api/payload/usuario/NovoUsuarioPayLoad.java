package com.postech.adjt.api.payload.usuario;

import java.util.List;

import com.postech.adjt.api.payload.EnderecoPayLoad;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NovoUsuarioPayLoad {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 50, message = "O nome deve ter até 50 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "O e-mail no formato inválido")
    @Size(max = 50, message = "O e-mail deve ter até 50 caracteres")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(max = 50, message = "A senha deve ter até 50 caracteres")
    private String senha;

    @NotNull(message = "O tipo do usuário não pode ser nulo")
    private TipoUsuarioPayLoad tipoUsuario;

    @NotNull(message = "O endereço não pode ser nulo")
    @Size(min = 1, message = "O endereço deve conter pelo menos um item")
    private List<EnderecoPayLoad> enderecos;
}
