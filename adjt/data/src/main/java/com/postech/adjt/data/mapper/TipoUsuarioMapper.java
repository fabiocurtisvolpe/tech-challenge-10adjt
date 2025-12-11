package com.postech.adjt.data.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;

/**
 * Componente responsável por mapear objetos entre TipoUsuarioEntidade e TipoUsuario.
 *
 * <p>
 * Realiza conversões bidirecionais entre {@link TipoUsuarioEntidade} e
 * {@link TipoUsuario}.
 * </p>
 *
 * @author Fabio
 * @since 2025-12-05
 */
@Component
public class TipoUsuarioMapper {

    /**
     * Converte uma entidade TipoUsuarioEntidade para o modelo de domínio TipoUsuario.
     *
     * @param entidade a entidade a ser convertida
     * @return o objeto TipoUsuario convertido, ou null se a entrada for nula
     */
    public static TipoUsuario toDomain(TipoUsuarioEntidade entidade) {
        if (entidade == null) {
            return null;
        }

        if (entidade.getIsDono()) {

            return TipoUsuarioDonoRestaurante.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .build();

        }

        return TipoUsuarioGenrico.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .build();
    }

    /**
     * Converte um objeto de domínio TipoUsuario para uma entidade TipoUsuarioEntidade.
     *
     * @param tipoUsuario o objeto de domínio a ser convertido
     * @return a entidade TipoUsuarioEntidade convertida, ou null se a entrada for nula
     */
    public static TipoUsuarioEntidade toEntity(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }

        Boolean isDono = tipoUsuario instanceof TipoUsuarioDonoRestaurante;

        TipoUsuarioEntidade entidade = new TipoUsuarioEntidade();
        entidade.setId(tipoUsuario.getId());
        entidade.setNome(tipoUsuario.getNome());
        entidade.setDescricao(tipoUsuario.getDescricao());
        entidade.setDataCriacao(tipoUsuario.getDataCriacao());
        entidade.setDataAlteracao(tipoUsuario.getDataAlteracao());
        entidade.setIsDono(isDono);

        return entidade;
    }

}
