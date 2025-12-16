package com.postech.adjt.domain.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.validators.CardapioValidator;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class CardapioFactory {

    public static Cardapio criar(String nome, String descricao, BigDecimal preco,
            String foto, Boolean disponivel, Restaurante restaurante, Integer idUsuarioLogado) throws IllegalArgumentException {

        Cardapio cardapio = Cardapio.builder()
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

        CardapioValidator.validar(cardapio, idUsuarioLogado);

        return cardapio;
    }

    public static Cardapio cardapio(Integer id, String nome, String descricao, BigDecimal preco,
            String foto, Restaurante restaurante, Boolean disponivel, 
            Boolean ativo, Integer idUsuarioLogado) throws IllegalArgumentException {

        Cardapio cardapio = Cardapio.builder()
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

        CardapioValidator.validar(cardapio, idUsuarioLogado);

        return cardapio;
    }

}
