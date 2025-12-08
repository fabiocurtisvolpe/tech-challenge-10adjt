package com.postech.adjt.domain.factory;

import java.time.LocalDateTime;

import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class CardapioFactory {

    public static Cardapio criar(String nome, String descricao, Double preco,
            String foto, Boolean disponivel, Restaurante restaurante) throws IllegalArgumentException {

        return Cardapio.builder()
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .foto(foto)
                .disponivel(disponivel)
                .restaurante(restaurante)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();
    }

    public static Cardapio atualizar(Integer id, String nome, String descricao, Double preco,
            String foto, Boolean disponivel, Restaurante restaurante, Boolean ativo) throws IllegalArgumentException {

        return Cardapio.builder()
                .id(id)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .foto(foto)
                .disponivel(disponivel)
                .restaurante(restaurante)
                .ativo(ativo)
                .dataAlteracao(LocalDateTime.now())
                .build();
    }

}
