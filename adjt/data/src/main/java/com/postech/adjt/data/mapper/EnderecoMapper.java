package com.postech.adjt.data.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.domain.entidade.Endereco;

/**
 * Componente responsável por mapear objetos entre EnderecoEntidade e Endereco.
 *
 * <p>
 * Realiza conversões bidirecionais entre {@link EnderecoEntidade} e
 * {@link Endereco}.
 * </p>
 *
 * @author Fabio
 * @since 2025-12-05
 */
@Component
public class EnderecoMapper {

    /**
     * Converte uma entidade EnderecoEntidade para o modelo de domínio Endereco.
     *
     * @param entidade a entidade a ser convertida
     * @return o objeto Endereco convertido, ou null se a entrada for nula
     */
    public static Endereco toDomain(EnderecoEntidade entidade) {
        if (entidade == null) {
            return null;
        }

        return Endereco.builder()
                .id(entidade.getId())
                .logradouro(entidade.getLogradouro())
                .numero(entidade.getNumero())
                .complemento(entidade.getComplemento())
                .bairro(entidade.getBairro())
                .pontoReferencia(entidade.getPontoReferencia())
                .cep(entidade.getCep())
                .municipio(entidade.getMunicipio())
                .uf(entidade.getUf())
                .principal(entidade.getPrincipal())
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .build();
    }

    /**
     * Converte um objeto de domínio Endereco para uma entidade EnderecoEntidade.
     *
     * @param endereco o objeto de domínio a ser convertido
     * @return a entidade EnderecoEntidade convertida, ou null se a entrada for nula
     */
    public static EnderecoEntidade toEntity(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        EnderecoEntidade entidade = new EnderecoEntidade();
        entidade.setId(endereco.getId());
        entidade.setLogradouro(endereco.getLogradouro());
        entidade.setNumero(endereco.getNumero());
        entidade.setComplemento(endereco.getComplemento());
        entidade.setBairro(endereco.getBairro());
        entidade.setPontoReferencia(endereco.getPontoReferencia());
        entidade.setCep(endereco.getCep());
        entidade.setMunicipio(endereco.getMunicipio());
        entidade.setUf(endereco.getUf());
        entidade.setPrincipal(endereco.getPrincipal());
        entidade.setDataCriacao(endereco.getDataCriacao());
        entidade.setDataAlteracao(endereco.getDataAlteracao());

        return entidade;
    }

}
