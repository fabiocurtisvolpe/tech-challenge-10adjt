package com.postech.adjt.api.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class TipoCozinhaRespostaDTO {

    private Integer id;
    private String nome;
    private String descricao;
    private LocalDateTime dataAlteracao;
    private Boolean ativo;

    public TipoCozinhaRespostaDTO(Integer id, String nome, String descricao, LocalDateTime dataAlteracao,
            Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataAlteracao = dataAlteracao;
        this.ativo = ativo;
    }
}
