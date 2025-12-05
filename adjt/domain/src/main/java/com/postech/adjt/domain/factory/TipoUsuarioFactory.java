package com.postech.adjt.domain.factory;

import java.time.LocalDateTime;

import com.postech.adjt.domain.entidade.TipoUsuario;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class TipoUsuarioFactory {

    public static TipoUsuario criar(String nome, String descricao) throws IllegalArgumentException {

        return TipoUsuario.builder()
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .ativo(true)    
                .nome(nome)
                .descricao(descricao)
                .build();
    }

    public static TipoUsuario atualizar(Integer id, String nome, String descricao, Boolean ativo) throws IllegalArgumentException {

        return TipoUsuario.builder()
                .id(id)
                .dataAlteracao(LocalDateTime.now())
                .ativo(ativo)    
                .nome(nome)
                .descricao(descricao)
                .build();
    }
}
