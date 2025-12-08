package com.postech.adjt.domain.factory;

import java.time.LocalDateTime;

import com.postech.adjt.domain.entidade.TipoCozinha;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class TipoCozinhaFactory {

    public static TipoCozinha criar(String nome, String descricao) throws IllegalArgumentException {

        return TipoCozinha.builder()
                .nome(nome)
                .descricao(descricao)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();
    }

    public static TipoCozinha atualizar(Integer id, String nome, String descricao,
            Boolean ativo) throws IllegalArgumentException {

        return TipoCozinha.builder()
                .id(id)
                .nome(nome)
                .descricao(descricao)
                .ativo(ativo)
                .dataAlteracao(LocalDateTime.now())
                .build();
    }
}
