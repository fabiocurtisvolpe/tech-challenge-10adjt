package com.postech.adjt.api.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrocarSenhaUsuarioPayLoad {

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(max = 50, message = "A senha deve ter até 50 caracteres")
    private String senha;
}
