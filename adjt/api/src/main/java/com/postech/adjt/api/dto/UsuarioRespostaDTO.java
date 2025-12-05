package com.postech.adjt.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.postech.adjt.domain.entidade.TipoUsuario;

import lombok.Getter;

/**
 * DTO de resposta para Usuario que oculta informações sensíveis
 * 
 * Não inclui: id, senha, dataCriacao, ativo
 * 
 * @author Fabio
 * @since 2025-12-01
 */

@Getter
public class UsuarioRespostaDTO {

    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private List<EnderecoRespostaDTO> enderecos;
    private LocalDateTime dataAlteracao;

    public UsuarioRespostaDTO(String nome, String email, TipoUsuario tipoUsuario,
            List<EnderecoRespostaDTO> enderecos, LocalDateTime dataAlteracao) {
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.enderecos = enderecos;
        this.dataAlteracao = dataAlteracao;
    }
}
