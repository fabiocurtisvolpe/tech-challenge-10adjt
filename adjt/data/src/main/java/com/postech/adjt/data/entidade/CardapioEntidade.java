package com.postech.adjt.data.entidade;

import java.math.BigDecimal;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
@Table(schema = "public", name = "cardapio")
public class CardapioEntidade extends BaseEntidade {

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    @Column(length = 2000)
    private String foto;

    @Column(nullable = false)
    private Boolean disponivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private RestauranteEntirade restaurante;
}
