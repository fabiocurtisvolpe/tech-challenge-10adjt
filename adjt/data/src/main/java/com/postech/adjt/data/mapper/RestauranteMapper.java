package com.postech.adjt.data.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.domain.entidade.Restaurante;

@Component
public class RestauranteMapper {

    public static Restaurante toDomain(RestauranteEntidade entidade) {
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

    public static RestauranteEntidade toEntity(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }

        RestauranteEntidade entidade = new RestauranteEntidade();
        entidade.setId(restaurante.getId());
        entidade.setAtivo(restaurante.getAtivo());
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
