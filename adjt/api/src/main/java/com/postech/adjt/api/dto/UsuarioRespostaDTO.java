package com.postech.adjt.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.postech.adjt.domain.enums.TipoUsuarioEnum;

/**
 * DTO de resposta para Usuario que oculta informações sensíveis
 * 
 * Não inclui: id, senha, dataCriacao, ativo
 * 
 * @author Fabio
 * @since 2025-12-01
 */
public class UsuarioRespostaDTO {

    private String nome;
    private String email;
    private TipoUsuarioEnum tipoUsuario;
    private List<EnderecoRespostaDTO> enderecos;
    private LocalDateTime dataAlteracao;

    public UsuarioRespostaDTO(String nome, String email, TipoUsuarioEnum tipoUsuario,
            List<EnderecoRespostaDTO> enderecos, LocalDateTime dataAlteracao) {
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.enderecos = enderecos;
        this.dataAlteracao = dataAlteracao;
    }

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

    public TipoUsuarioEnum getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuarioEnum tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<EnderecoRespostaDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoRespostaDTO> enderecos) {
        this.enderecos = enderecos;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
