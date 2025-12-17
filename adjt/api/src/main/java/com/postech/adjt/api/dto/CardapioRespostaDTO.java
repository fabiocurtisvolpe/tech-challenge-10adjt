package com.postech.adjt.api.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CardapioRespostaDTO {

    private Integer id;
    private String nome;
    private String descricao;
    private LocalDateTime dataAlteracao;
    protected BigDecimal preco;
    protected String foto;
    protected Boolean disponivel;
    private Boolean ativo;
    private RestauranteRespostaDTO restaurante;

    public CardapioRespostaDTO(Integer id, String nome, String descricao, LocalDateTime dataAlteracao,
                               BigDecimal preco, String foto, Boolean disponivel, Boolean ativo,
                               RestauranteRespostaDTO restaurante) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataAlteracao = dataAlteracao;
        this.preco = preco;
        this.foto = foto;
        this.disponivel = disponivel;
        this.ativo = ativo;
        this.restaurante = restaurante;
    }
}
