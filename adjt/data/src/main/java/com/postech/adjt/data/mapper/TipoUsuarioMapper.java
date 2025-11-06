package com.postech.adjt.data.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entity.TipoUsuarioEntity;
import com.postech.adjt.domain.model.TipoUsuario;

/**
 * Componente responsável por mapear objetos entre a entidade
 * {@link TipoUsuario}
 * e o Model {@link TipoUsuario}.
 *
 * <p>
 * Utilizado para transformar dados entre as camadas de persistência e
 * apresentação,
 * garantindo que apenas as informações necessárias sejam expostas ou recebidas.
 * </p>
 *
 * @author Fabio
 * @since 2025-11-06
 */
@Component
public class TipoUsuarioMapper {

    /**
     * Converte uma entidade de domínio {@link TipoUsuario} em uma entidade JPA
     * {@link TipoUsuarioEntity}.
     *
     * @param domain Entidade de domínio a ser convertida
     * @return Entidade JPA correspondente, ou null se a entrada for nula
     */
    public TipoUsuarioEntity toEntity(TipoUsuario domain) {
        if (Objects.isNull(domain)) {
            return null;
        }

        TipoUsuarioEntity entity = new TipoUsuarioEntity();
        entity.setId(domain.getId());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setDataAlteracao(domain.getDataAlteracao());
        entity.setAtivo(domain.getAtivo());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());

        return entity;
    }

    /**
     * Converte uma entidade JPA {@link TipoUsuarioEntity} em uma entidade de
     * domínio
     * {@link TipoUsuario}.
     *
     * @param entity Entidade JPA a ser convertida
     * @return Entidade de domínio correspondente, ou null se a entrada for nula
     */
    public TipoUsuario toDomain(TipoUsuarioEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        TipoUsuario domain = new TipoUsuario();
        domain.setId(entity.getId());
        domain.setDataCriacao(entity.getDataCriacao());
        domain.setDataAlteracao(entity.getDataAlteracao());
        domain.setAtivo(entity.getAtivo());
        domain.setNome(entity.getNome());
        domain.setDescricao(entity.getDescricao());

        return domain;
    }
}
