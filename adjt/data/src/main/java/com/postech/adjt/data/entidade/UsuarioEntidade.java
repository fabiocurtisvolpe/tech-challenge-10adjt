package com.postech.adjt.data.entidade;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
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
@Table(schema = "public", name = "usuario")
public class UsuarioEntidade extends BaseEntidade {

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "e_mail", nullable = false, length = 100)
    private String email;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_usuario_id", nullable = false)
    private TipoUsuarioEntidade tipoUsuario;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnderecoEntidade> enderecos = new ArrayList<>();


    public void adicionarEndereco(EnderecoEntidade endereco) {
        endereco.setUsuario(this);
        this.enderecos.add(endereco);
    }
}
