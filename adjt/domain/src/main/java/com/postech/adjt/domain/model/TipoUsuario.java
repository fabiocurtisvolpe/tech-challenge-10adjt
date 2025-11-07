package com.postech.adjt.domain.model;

public class TipoUsuario extends BaseModel {

    private String nome;
    private String descricao;
    private Boolean podeSerExcluido;

    public TipoUsuario(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public TipoUsuario(Integer id, Boolean ativo, Boolean podeSerExcluido, String nome, String descricao) {
        this.setId(id);
        this.setAtivo(ativo);
        this.podeSerExcluido = podeSerExcluido;
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Boolean getPodeSerExcluido() {
        return podeSerExcluido;
    }
}
