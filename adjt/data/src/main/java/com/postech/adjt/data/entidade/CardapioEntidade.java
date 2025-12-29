package com.postech.adjt.data.entidade;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

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
    private RestauranteEntidade restaurante;

    @Override
    protected void onCreate() {
        super.onCreate();
        this.ajustarPreco();
    }

    @PreUpdate
    protected void ajustarPreco() {
        if (this.preco != null) {
            this.preco = this.preco.setScale(2, java.math.RoundingMode.HALF_UP);
        }
    }
}
