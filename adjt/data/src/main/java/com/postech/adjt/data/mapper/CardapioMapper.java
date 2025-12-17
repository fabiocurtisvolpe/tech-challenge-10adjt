package com.postech.adjt.data.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.CardapioEntidade;
import com.postech.adjt.domain.entidade.Cardapio;

@Component
public class CardapioMapper {

    public static Cardapio toDomain(CardapioEntidade entidade) {
        if (entidade == null) {
            return null;
        }

        return Cardapio.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .preco(entidade.getPreco())
                .foto(entidade.getFoto())
                .disponivel(entidade.getDisponivel())
                .restaurante(RestauranteMapper.toDomain(entidade.getRestaurante()))
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .build();
    }

    public static CardapioEntidade toEntity(Cardapio cardapio) {
        if (cardapio == null) {
            return null;
        }

        CardapioEntidade entidade = new CardapioEntidade();
        entidade.setId(cardapio.getId());
        entidade.setNome(cardapio.getNome());
        entidade.setDescricao(cardapio.getDescricao());
        entidade.setPreco(cardapio.getPreco());
        entidade.setFoto(cardapio.getFoto());
        entidade.setDisponivel(cardapio.getDisponivel());
        entidade.setRestaurante(RestauranteMapper.toEntity(cardapio.getRestaurante()));
        entidade.setDataCriacao(cardapio.getDataCriacao());
        entidade.setDataAlteracao(cardapio.getDataAlteracao());

        return entidade;
    }

}
