package com.postech.adjt.data.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.TipoCozinhaEntidade;
import com.postech.adjt.domain.entidade.TipoCozinha;

/**
 * Componente responsável por mapear objetos entre TipoCozinhaEntidade e TipoCozinha.
 *
 * <p>
 * Realiza conversões bidirecionais entre {@link TipoCozinhaEntidade} e
 * {@link TipoCozinha}.
 * </p>
 *
 * @author Fabio
 * @since 2025-12-05
 */
@Component
public class TipoCozinhaMapper {

    /**
     * Converte uma entidade TipoCozinhaEntidade para o modelo de domínio TipoCozinha.
     *
     * @param entidade a entidade a ser convertida
     * @return o objeto TipoCozinha convertido, ou null se a entrada for nula
     */
    public static TipoCozinha toDomain(TipoCozinhaEntidade entidade) {
        if (entidade == null) {
            return null;
        }

        return TipoCozinha.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .build();
    }

    /**
     * Converte um objeto de domínio TipoCozinha para uma entidade TipoCozinhaEntidade.
     *
     * @param tipoCozinha o objeto de domínio a ser convertido
     * @return a entidade TipoCozinhaEntidade convertida, ou null se a entrada for nula
     */
    public static TipoCozinhaEntidade toEntity(TipoCozinha tipoCozinha) {
        if (tipoCozinha == null) {
            return null;
        }

        TipoCozinhaEntidade entidade = new TipoCozinhaEntidade();
        entidade.setId(tipoCozinha.getId());
        entidade.setNome(tipoCozinha.getNome());
        entidade.setDescricao(tipoCozinha.getDescricao());
        entidade.setDataCriacao(tipoCozinha.getDataCriacao());
        entidade.setDataAlteracao(tipoCozinha.getDataAlteracao());

        return entidade;
    }

}
