package com.postech.adjt.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.postech.adjt.domain.enums.TipoUsuarioEnum;

public class Usuario extends BaseModel {

    private String nome;
    private String email;
    private String senha;
    private TipoUsuarioEnum tipoUsuario;
    private List<Endereco> enderecos = new ArrayList<>();

    public Usuario(String nome, String email, String senha, TipoUsuarioEnum tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario(String nome, String email, String senha, TipoUsuarioEnum tipoUsuario, List<Endereco> enderecos) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.enderecos = enderecos;
    }

    public Usuario(Integer id, String nome, String email, String senha, TipoUsuarioEnum tipoUsuario,
            List<Endereco> enderecos) {
        this.setId(id);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.enderecos = enderecos;
    }

    public Usuario(Integer id, Boolean ativo, String nome, String email, String senha, TipoUsuarioEnum tipoUsuario,
            List<Endereco> enderecos) {
        this.setId(id);
        this.setAtivo(ativo);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
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

    public TipoUsuarioEnum getTipoUsuario() {
        return tipoUsuario;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public Boolean getEhDonoRestaurante() {
        return this.tipoUsuario == TipoUsuarioEnum.DONO_RESTAURANTE;
    }
}
