package com.postech.adjt.data.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;

@Component
public class TipoUsuarioMapper {

    public static TipoUsuario toDomain(TipoUsuarioEntidade entidade) {
        if (entidade == null) {
            return null;
        }

        Restaurante restaurante = RestauranteMapper.toDomain(entidade.getRestaurante());

        if (entidade.getIsDono()) {

            return TipoUsuarioDonoRestaurante.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .ativo(entidade.getAtivo())
                .isEditavel(entidade.getIsEditavel())
                .isDono(entidade.getIsDono())
                .restaurante(restaurante)
                .build();

        }

        return TipoUsuarioGenrico.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .ativo(entidade.getAtivo())
                .isEditavel(entidade.getIsEditavel())
                .isDono(entidade.getIsDono())
                .restaurante(restaurante)
                .build();
    }

    public static TipoUsuarioEntidade toEntity(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }

        Boolean isDono = tipoUsuario instanceof TipoUsuarioDonoRestaurante;
        Boolean ativo = tipoUsuario.getAtivo() != null ? tipoUsuario.getAtivo() : true;

        RestauranteEntidade restaurante = RestauranteMapper.toEntity(tipoUsuario.getRestaurante());

        TipoUsuarioEntidade entidade = new TipoUsuarioEntidade();
        entidade.setId(tipoUsuario.getId());
        entidade.setNome(tipoUsuario.getNome());
        entidade.setDescricao(tipoUsuario.getDescricao());
        entidade.setDataCriacao(tipoUsuario.getDataCriacao());
        entidade.setDataAlteracao(tipoUsuario.getDataAlteracao());
        entidade.setAtivo(ativo);
        entidade.setIsDono(isDono);
        entidade.setIsEditavel(true);
        entidade.setRestaurante(restaurante);

        return entidade;
    }
}
