package com.postech.adjt.api.mapper;

import com.postech.adjt.api.dto.CardapioRespostaDTO;
import com.postech.adjt.api.dto.EnderecoRespostaDTO;
import com.postech.adjt.api.dto.RestauranteRespostaDTO;
import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.EnderecoPayLoad;
import com.postech.adjt.api.payload.cardapio.AtualizaCardapioPayLoad;
import com.postech.adjt.api.payload.cardapio.NovoCardapioPayLoad;
import com.postech.adjt.api.payload.restaurante.AtualizaRestaurantePayLoad;
import com.postech.adjt.api.payload.restaurante.NovoRestaurantePayLoad;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.dto.EnderecoDTO;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class CardapioMapperApi {

    public static CardapioDTO toNovoCardapioDTO(NovoCardapioPayLoad payload, String usuarioLogado) {

        UsuarioDTO dono = toDono(usuarioLogado);

        RestauranteDTO restaurante = new RestauranteDTO(
                payload.getIdRestaurante(),
                null,
                null,
                null,
                null,
                null,
                dono, true);

        return new CardapioDTO(null,
                payload.getNome(),
                payload.getDescricao(),
                payload.getPreco(),
                payload.getFoto(),
                payload.getDisponivel(),
                restaurante);
    }

    public static CardapioDTO toAtualizaCardapioDTO(AtualizaCardapioPayLoad payload, String usuarioLogado) {

        UsuarioDTO dono = toDono(usuarioLogado);

        RestauranteDTO restaurante = new RestauranteDTO(
                payload.getIdRestaurante(),
                null,
                null,
                null,
                null,
                null,
                dono, true);

        return new CardapioDTO(payload.getId(),
                payload.getNome(),
                payload.getDescricao(),
                payload.getPreco(),
                payload.getFoto(),
                payload.getDisponivel(),
                restaurante);
    }

    public static CardapioRespostaDTO toCardapioRespostaDTO(Cardapio cardapio) {
        if (cardapio == null) {
            return null;
        }

        RestauranteRespostaDTO restaurante = RestauranteMapperApi.toRestauranteRespostaGeralDTO(cardapio.getRestaurante());

        return new CardapioRespostaDTO(cardapio.getId(), cardapio.getNome(), cardapio.getDescricao(),
                cardapio.getDataAlteracao(), cardapio.getPreco(), cardapio.getFoto(),
                cardapio.getDisponivel(), cardapio.getAtivo(),
                restaurante);

    }

    private static UsuarioDTO toDono(String usuarioLogado) {
        return new UsuarioDTO(null,
                null,
                usuarioLogado,
                null,
                null,
                null,
                null);
    }
}
