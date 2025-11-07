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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public void adicionarEndereco(Endereco endereco) {
        endereco.setUsuario(this);
        this.enderecos.add(endereco);
    }

    public Boolean getEhDonoRestaurante() {
        return ehDonoRestaurante;
    }

    public void setEhDonoRestaurante(Boolean ehDonoRestaurante) {
        this.ehDonoRestaurante = ehDonoRestaurante;
    }
}
