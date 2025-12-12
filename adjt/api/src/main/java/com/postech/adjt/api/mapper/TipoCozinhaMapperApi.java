package com.postech.adjt.api.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.TipoCozinhaRespostaDTO;
import com.postech.adjt.api.payload.AtualizaTipoCozinhaPayLoad;
import com.postech.adjt.api.payload.NovoTipoCozinhaPayLoad;
import com.postech.adjt.domain.dto.TipoCozinhaDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.factory.TipoCozinhaFactory;

@Component
public class TipoCozinhaMapperApi {

        public static TipoCozinhaDTO toNovoTipoCozinhaDTO(NovoTipoCozinhaPayLoad payload) {

                TipoCozinha tipoCozinha = TipoCozinhaFactory.novo(payload.getNome(), payload.getDescricao());

                return new TipoCozinhaDTO(
                                null,
                                tipoCozinha.getNome(),
                                tipoCozinha.getDescricao(), true);
        }

        public static TipoCozinhaDTO toAtualizaTipoCozinhaDTO(AtualizaTipoCozinhaPayLoad payload) {

                TipoCozinha tipoCozinha = TipoCozinhaFactory.tipoCozinha(payload.getId(), payload.getNome(),
                                payload.getDescricao(), payload.getAtivo());

                return new TipoCozinhaDTO(
                                tipoCozinha.getId(),
                                tipoCozinha.getNome(),
                                tipoCozinha.getDescricao(),
                                tipoCozinha.getAtivo());
        }

        public static TipoCozinhaRespostaDTO toTipoCozinhaRespostaDTO(TipoCozinha tipoCozinha) {
                if (tipoCozinha == null) {
                        return null;
                }

                return new TipoCozinhaRespostaDTO(
                                tipoCozinha.getId(),
                                tipoCozinha.getNome(),
                                tipoCozinha.getDescricao(),
                                tipoCozinha.getDataAlteracao(),
                                tipoCozinha.getAtivo());
        }
}
