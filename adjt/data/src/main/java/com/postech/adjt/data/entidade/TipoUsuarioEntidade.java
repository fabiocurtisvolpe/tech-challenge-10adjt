package com.postech.adjt.data.entidade;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
@Table(schema = "public", name = "tipo_usuario")
public class TipoUsuarioEntidade extends BaseEntidade {

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "descricao", length = 200)
    private String descricao;

    @OneToMany(mappedBy = "tipoUsuario", fetch = FetchType.LAZY)
    private List<UsuarioEntidade> usuarios = new ArrayList<>();

    @Column(name = "dono")
    private Boolean isDono;

    @Column(name = "editavel")
    private Boolean isEditavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = true)
    private RestauranteEntidade restaurante;
}
