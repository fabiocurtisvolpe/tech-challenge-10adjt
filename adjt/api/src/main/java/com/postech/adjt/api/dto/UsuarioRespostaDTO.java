package com.postech.adjt.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.postech.adjt.domain.entidade.TipoUsuario;

import lombok.Getter;

@Getter
public class UsuarioRespostaDTO {

    private Integer id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private List<EnderecoRespostaDTO> enderecos;
    private LocalDateTime dataAlteracao;

    public UsuarioRespostaDTO(Integer id, String nome, String email, TipoUsuario tipoUsuario,
            List<EnderecoRespostaDTO> enderecos, LocalDateTime dataAlteracao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.enderecos = enderecos;
        this.dataAlteracao = dataAlteracao;
    }
}
