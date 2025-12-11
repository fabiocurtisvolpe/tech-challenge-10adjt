package com.postech.adjt.api.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NovoTipoUsuarioPayLoad {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 50, message = "O nome deve ter até 50 caracteres")
    protected String nome;

    @Size(max = 1000, message = "A descrição deve ter até 1000 caracteres")
    protected String descricao;
}
