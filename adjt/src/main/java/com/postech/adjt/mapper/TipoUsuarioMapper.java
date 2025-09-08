package com.postech.adjt.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.model.TipoUsuario;

/**
 * Componente responsável por mapear objetos entre a entidade
 * {@link TipoUsuario}
 * e o DTO {@link TipoUsuarioDTO}.
 *
 * <p>
 * Utilizado para transformar dados entre as camadas de persistência e
 * apresentação,
 * garantindo que apenas as informações necessárias sejam expostas ou recebidas.
 * </p>
 *
 * <p>
 * Esse mapper é utilizado principalmente pelo {@link UsuarioMapper} para compor
 * o mapeamento completo de um {@link com.postech.adjt.model.Usuario}.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Component
public class TipoUsuarioMapper {

    /**
     * Converte uma entidade {@link TipoUsuario} em um objeto
     * {@link TipoUsuarioDTO}.
     *
     * @param entidade Entidade {@link TipoUsuario} a ser convertida.
     * @return Objeto {@link TipoUsuarioDTO} correspondente, ou {@code null} se a
     *         entidade for nula.
     */
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

    /**
     * Converte um objeto {@link TipoUsuarioDTO} em uma entidade
     * {@link TipoUsuario}.
     *
     * @param dto Objeto {@link TipoUsuarioDTO} a ser convertido.
     * @return Entidade {@link TipoUsuario} correspondente, ou {@code null} se o DTO
     *         for nulo.
     */
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
