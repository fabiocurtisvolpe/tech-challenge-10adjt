package com.postech.adjt.domain.factory;

import java.time.LocalDateTime;

import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class TipoUsuarioFactory {

    public static TipoUsuario novo(String nome, String descricao, Boolean isDono) throws IllegalArgumentException {

        if (isDono) {
            return TipoUsuarioDonoRestaurante.builder()
                    .dataCriacao(LocalDateTime.now())
                    .dataAlteracao(LocalDateTime.now())
                    .ativo(true)    
                    .nome(nome)
                    .descricao(descricao)
                    .build();
        }

        return TipoUsuarioGenrico.builder()
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .ativo(true)    
                .nome(nome)
                .descricao(descricao)
                .build();
    }

    public static TipoUsuario tipoUsuario(Integer id, String nome, String descricao, 
        Boolean ativo, Boolean isDono) throws IllegalArgumentException {

        if (isDono) {
            return TipoUsuarioDonoRestaurante.builder()
                .id(id)
                .dataAlteracao(LocalDateTime.now())
                .ativo(ativo)    
                .nome(nome)
                .descricao(descricao)
                .build();
        }

        return TipoUsuarioGenrico.builder()
                .id(id)
                .dataAlteracao(LocalDateTime.now())
                .ativo(ativo)    
                .nome(nome)
                .descricao(descricao)
                .build();
    }
}
