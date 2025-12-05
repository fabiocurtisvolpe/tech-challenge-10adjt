package com.postech.adjt.api.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TipoUsuarioPayLoad {
    private Integer id;
    private String nome;
    private String descricao;
}
