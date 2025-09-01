package com.postech.adjt.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.model.TipoUsuario;

@Component
public class TipoUsuarioMapper {

    public TipoUsuarioDTO toTipoUsuarioDTO(TipoUsuario entidade) {
        if (Objects.isNull(entidade)) {
            return null;
        }

        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setId(entidade.getId());
        dto.setDataCriacao(entidade.getDataAlteracao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setAtivo(entidade.getAtivo());
        dto.setNome(entidade.getNome());
        dto.setDescricao(entidade.getDescricao());
    
        return dto;
    }

    public TipoUsuario toTipoUsuario(TipoUsuarioDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        TipoUsuario entidade = new TipoUsuario();
        entidade.setId(dto.getId());
        entidade.setDataCriacao(dto.getDataAlteracao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setAtivo(dto.getAtivo());
        entidade.setNome(dto.getNome());
        entidade.setDescricao(dto.getDescricao());

        return entidade;
    }
}
