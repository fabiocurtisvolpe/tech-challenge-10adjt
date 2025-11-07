package com.postech.adjt.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario extends BaseModel {

    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;
    private List<Endereco> enderecos = new ArrayList<>();
    private Boolean ehDonoRestaurante;

    public Usuario(String nome, String email, String senha, TipoUsuario tipoUsuario, Boolean ehDonoRestaurante,
            List<Endereco> enderecos) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.ehDonoRestaurante = ehDonoRestaurante;
        this.enderecos = enderecos;
    }

    public Usuario(Integer id, Boolean ativo, String nome, String email, String senha, TipoUsuario tipoUsuario,
            Boolean ehDonoRestaurante, List<Endereco> enderecos) {
        this.setId(id);
        this.setAtivo(ativo);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.ehDonoRestaurante = ehDonoRestaurante;
        this.enderecos = enderecos;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    /*
     * public void adicionarEndereco(Endereco endereco) {
     * endereco.setUsuario(this);
     * this.enderecos.add(endereco);
     * }
     */

    public Boolean getEhDonoRestaurante() {
        return ehDonoRestaurante;
    }
}
