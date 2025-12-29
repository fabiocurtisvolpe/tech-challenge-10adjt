package com.postech.adjt.data.entidade;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.Audited;

import com.postech.adjt.domain.enums.TipoCozinhaEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
@Table(schema = "public", name = "restaurante")
public class RestauranteEntidade extends BaseEntidade {

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "horario_funcionamento", nullable = false)
    private String horarioFuncionamento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", nullable = false, unique = true)
    private EnderecoEntidade endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cozinha")
    private TipoCozinhaEnum tipoCozinha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_dono", nullable = false)
    private UsuarioEntidade dono;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardapioEntidade> itensCardapio = new ArrayList<>();

     public void adicionarCardapio(CardapioEntidade cardapio) {
        cardapio.setRestaurante(this);
        this.itensCardapio.add(cardapio);
    }
}
