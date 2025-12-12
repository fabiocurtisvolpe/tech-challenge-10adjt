package com.postech.adjt.api.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class RestauranteRespostaDTO {

    private Integer id;
    private String nome;
    private String descricao;
    private LocalDateTime dataAlteracao;
    private EnderecoRespostaDTO endereco;
    private TipoCozinhaRespostaDTO tipoCozinha;
    private UsuarioRespostaDTO dono;
    private Boolean ativo;

    public RestauranteRespostaDTO(Integer id, String nome, String descricao, LocalDateTime dataAlteracao,
            EnderecoRespostaDTO endereco, TipoCozinhaRespostaDTO tipoCozinha, UsuarioRespostaDTO dono, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataAlteracao = dataAlteracao;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.dono = dono;
        this.ativo = ativo;
    }
}
