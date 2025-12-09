package com.postech.adjt.data.entidade;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
@Table(schema = "public", name = "tipo_cozinha")
public class TipoCozinhaEntidade extends BaseEntidade {

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "descricao", length = 1000)
    private String descricao;

}
