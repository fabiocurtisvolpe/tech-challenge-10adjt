package com.postech.adjt.domain.factory;

import java.time.LocalDateTime;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class RestauranteFactory {

    public static Restaurante criar(String nome, String descricao, String horarioFuncionamento,
            TipoCozinhaEnum tipoCozinha, Endereco endereco, Usuario dono) throws IllegalArgumentException {

        return Restaurante.builder()
                .nome(nome)
                .descricao(descricao)
                .horarioFuncionamento(horarioFuncionamento)
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();
    }

    public static Restaurante atualizar(Integer id, String nome, String descricao, String horarioFuncionamento,
            TipoCozinhaEnum tipoCozinha, Endereco endereco, Usuario dono, Boolean ativo) throws IllegalArgumentException {

        return Restaurante.builder()
                .id(id)
                .nome(nome)
                .descricao(descricao)
                .horarioFuncionamento(horarioFuncionamento)
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .ativo(ativo)
                .dataAlteracao(LocalDateTime.now())
                .build();
    }

}
