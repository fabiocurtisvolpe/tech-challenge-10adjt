package com.postech.adjt.data.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.CardapioEntidade;
import com.postech.adjt.domain.entidade.Cardapio;

/**
 * Componente responsável por mapear objetos entre CardapioEntidade e Cardapio.
 *
 * <p>
 * Realiza conversões bidirecionais entre {@link CardapioEntidade} e
 * {@link Cardapio}, incluindo mapeamento de entidades relacionadas.
 * </p>
 *
 * @author Fabio
 * @since 2025-12-05
 */
@Component
public class CardapioMapper {

    /**
     * Converte uma entidade CardapioEntidade para o modelo de domínio Cardapio.
     *
     * @param entidade a entidade a ser convertida
     * @return o objeto Cardapio convertido, ou null se a entrada for nula
     */
    public static Cardapio toDomain(CardapioEntidade entidade) {
        if (entidade == null) {
            return null;
        }

        return Cardapio.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .preco(entidade.getPreco() != null ? entidade.getPreco().doubleValue() : null)
                .foto(entidade.getFoto())
                .disponivel(entidade.getDisponivel())
                .restaurante(RestauranteMapper.toDomain(entidade.getRestaurante()))
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .build();
    }

    /**
     * Converte um objeto de domínio Cardapio para uma entidade CardapioEntidade.
     *
     * @param cardapio o objeto de domínio a ser convertido
     * @return a entidade CardapioEntidade convertida, ou null se a entrada for nula
     */
    public static CardapioEntidade toEntity(Cardapio cardapio) {
        if (cardapio == null) {
            return null;
        }

        CardapioEntidade entidade = new CardapioEntidade();
        entidade.setId(cardapio.getId());
        entidade.setNome(cardapio.getNome());
        entidade.setDescricao(cardapio.getDescricao());
        entidade.setPreco(cardapio.getPreco() != null ? BigDecimal.valueOf(cardapio.getPreco()) : null);
        entidade.setFoto(cardapio.getFoto());
        entidade.setDisponivel(cardapio.getDisponivel());
        entidade.setRestaurante(RestauranteMapper.toEntity(cardapio.getRestaurante()));
        entidade.setDataCriacao(cardapio.getDataCriacao());
        entidade.setDataAlteracao(cardapio.getDataAlteracao());

        return entidade;
    }

}
