package com.postech.adjt.api.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoCozinhaPayLoad {

    @NotNull(message = "O id do tipo de cozinha não pode estar vazio")
    private Integer id;
    private String nome;
    private String descricao;
}
