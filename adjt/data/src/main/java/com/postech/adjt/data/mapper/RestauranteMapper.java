package com.postech.adjt.data.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.RestauranteEntirade;
import com.postech.adjt.domain.entidade.Restaurante;

/**
 * Componente responsável por mapear objetos entre RestauranteEntirade e Restaurante.
 *
 * <p>
 * Realiza conversões bidirecionais entre {@link RestauranteEntirade} e
 * {@link Restaurante}, incluindo mapeamento de entidades relacionadas como
 * TipoCozinha, Endereco e Usuario (dono).
 * </p>
 *
 * @author Fabio
 * @since 2025-12-05
 */
@Component
public class RestauranteMapper {

    /**
     * Converte uma entidade RestauranteEntirade para o modelo de domínio Restaurante.
     *
     * @param entidade a entidade a ser convertida
     * @return o objeto Restaurante convertido, ou null se a entrada for nula
     */
    public static Restaurante toDomain(RestauranteEntirade entidade) {
        if (entidade == null) {
            return null;
        }

        return Restaurante.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .horarioFuncionamento(entidade.getHorarioFuncionamento())
                .tipoCozinha(entidade.getTipoCozinha())
                .endereco(EnderecoMapper.toDomain(entidade.getEndereco()))
                .dono(UsuarioMapper.toDomain(entidade.getDono()))
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .build();
    }

    /**
     * Converte um objeto de domínio Restaurante para uma entidade RestauranteEntirade.
     *
     * @param restaurante o objeto de domínio a ser convertido
     * @return a entidade RestauranteEntirade convertida, ou null se a entrada for nula
     */
    public static RestauranteEntirade toEntity(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }

        RestauranteEntirade entidade = new RestauranteEntirade();
        entidade.setId(restaurante.getId());
        entidade.setNome(restaurante.getNome());
        entidade.setDescricao(restaurante.getDescricao());
        entidade.setHorarioFuncionamento(restaurante.getHorarioFuncionamento());
        entidade.setTipoCozinha(restaurante.getTipoCozinha());
        entidade.setEndereco(EnderecoMapper.toEntity(restaurante.getEndereco()));
        entidade.setDono(UsuarioMapper.toEntity(restaurante.getDono()));
        entidade.setDataCriacao(restaurante.getDataCriacao());
        entidade.setDataAlteracao(restaurante.getDataAlteracao());

        return entidade;
    }

}
