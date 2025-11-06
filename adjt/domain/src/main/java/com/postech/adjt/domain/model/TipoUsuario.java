package com.postech.adjt.domain.model;

import java.util.ArrayList;
import java.util.List;

public class TipoUsuario extends BaseModel {

    private String nome;
    private String descricao;
    private List<Usuario> usuarios = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
