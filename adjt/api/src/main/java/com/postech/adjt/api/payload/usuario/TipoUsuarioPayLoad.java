package com.postech.adjt.api.payload.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TipoUsuarioPayLoad {

    @NotNull(message = "O id do tipo de usuário não pode estar vazio")
    private Integer id;
    private String nome;
    private String descricao;

    @NotNull(message = "O campo dono não pode estar vazio")
    private Boolean isDono;
}
