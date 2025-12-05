package com.postech.adjt.data.entidade;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(schema = "public", name = "endereco")
public class EnderecoEntidade extends BaseEntidade {

    @Column(name = "logradouro", nullable = false, length = 200)
    private String logradouro;

    @Column(name = "numero", length = 5)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    @Column(name = "ponto_referencia", length = 100)
    private String pontoReferencia;

    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    @Column(name = "municipio", nullable = false, length = 100)
    private String municipio;

    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @Column(name = "principal", nullable = false)
    private Boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioEntidade usuario;
}
