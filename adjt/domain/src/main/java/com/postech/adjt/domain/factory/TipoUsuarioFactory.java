package com.postech.adjt.domain.factory;

import java.time.LocalDateTime;

import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;
import com.postech.adjt.domain.validators.TipoUsuarioValidator;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class TipoUsuarioFactory {

    public static TipoUsuario novo(String nome, String descricao, Boolean isDono,
            Restaurante restaurante, Integer idUsuarioLogado) throws IllegalArgumentException {

        TipoUsuario tipoUsuario;

        if (isDono) {
            tipoUsuario = TipoUsuarioDonoRestaurante.builder()
                    .dataCriacao(LocalDateTime.now())
                    .dataAlteracao(LocalDateTime.now())
                    .ativo(true)
                    .nome(nome)
                    .descricao(descricao)
                    .restaurante(restaurante)
                    .isEditavel(true)
                    .build();
        }

        tipoUsuario = TipoUsuarioGenrico.builder()
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .ativo(true)
                .nome(nome)
                .descricao(descricao)
                .restaurante(restaurante)
                .isEditavel(true)
                .build();

        TipoUsuarioValidator.validar(tipoUsuario, idUsuarioLogado);

        return tipoUsuario;
    }

    public static TipoUsuario tipoUsuario(Integer id, String nome, String descricao,
            Boolean ativo, Boolean isDono, Restaurante restaurante, Integer idUsuarioLogado)
            throws IllegalArgumentException {

        TipoUsuario tipoUsuario;

        if (isDono) {
            tipoUsuario = TipoUsuarioDonoRestaurante.builder()
                    .id(id)
                    .dataAlteracao(LocalDateTime.now())
                    .ativo(ativo)
                    .nome(nome)
                    .descricao(descricao)
                    .restaurante(restaurante)
                    .isEditavel(true)
                    .build();
        }

        tipoUsuario = TipoUsuarioGenrico.builder()
                .id(id)
                .dataAlteracao(LocalDateTime.now())
                .ativo(ativo)
                .nome(nome)
                .descricao(descricao)
                .restaurante(restaurante)
                .isEditavel(true)
                .build();

        TipoUsuarioValidator.validar(tipoUsuario, idUsuarioLogado);

        return tipoUsuario;
    }
}
