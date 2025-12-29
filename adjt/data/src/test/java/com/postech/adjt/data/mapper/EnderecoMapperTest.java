package com.postech.adjt.data.mapper;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.domain.entidade.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EnderecoMapperTest {

    @Test
    @DisplayName("Deve converter EnderecoEntidade para Endereco (Domain) corretamente")
    void deveConverterEntidadeParaDominio() {

        LocalDateTime agora = LocalDateTime.now();

        EnderecoEntidade entidade = new EnderecoEntidade();
        entidade.setId(1);
        entidade.setLogradouro("Av. Paulista");
        entidade.setNumero("1000");
        entidade.setComplemento("Apto 10");
        entidade.setBairro("Bela Vista");
        entidade.setPontoReferencia("Perto do MASP");
        entidade.setCep("01310-100");
        entidade.setMunicipio("São Paulo");
        entidade.setUf("SP");
        entidade.setPrincipal(true);
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);

        Endereco domain = EnderecoMapper.toDomain(entidade);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(entidade.getId());
        assertThat(domain.getLogradouro()).isEqualTo(entidade.getLogradouro());
        assertThat(domain.getNumero()).isEqualTo(entidade.getNumero());
        assertThat(domain.getComplemento()).isEqualTo(entidade.getComplemento());
        assertThat(domain.getBairro()).isEqualTo(entidade.getBairro());
        assertThat(domain.getPontoReferencia()).isEqualTo(entidade.getPontoReferencia());
        assertThat(domain.getCep()).isEqualTo(entidade.getCep());
        assertThat(domain.getMunicipio()).isEqualTo(entidade.getMunicipio());
        assertThat(domain.getUf()).isEqualTo(entidade.getUf());
        assertThat(domain.getPrincipal()).isEqualTo(entidade.getPrincipal());
        assertThat(domain.getDataCriacao()).isEqualTo(entidade.getDataCriacao());
        assertThat(domain.getDataAlteracao()).isEqualTo(entidade.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve retornar null ao converter Entidade nula para Dominio")
    void deveRetornarNullQuandoEntidadeNula() {
        Endereco domain = EnderecoMapper.toDomain(null);
        assertThat(domain).isNull();
    }

    @Test
    @DisplayName("Deve converter Endereco (Domain) para EnderecoEntidade corretamente")
    void deveConverterDominioParaEntidade() {
        
        LocalDateTime agora = LocalDateTime.now();

        Endereco domain = Endereco.builder()
                .id(2)
                .logradouro("Rua das Flores")
                .numero("50")
                .complemento("Casa")
                .bairro("Jardim")
                .pontoReferencia("Praça")
                .cep("12345-678")
                .municipio("Campinas")
                .uf("SP")
                .principal(false)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        EnderecoEntidade entidade = EnderecoMapper.toEntity(domain);

        assertThat(entidade).isNotNull();
        assertThat(entidade.getId()).isEqualTo(domain.getId());
        assertThat(entidade.getLogradouro()).isEqualTo(domain.getLogradouro());
        assertThat(entidade.getNumero()).isEqualTo(domain.getNumero());
        assertThat(entidade.getComplemento()).isEqualTo(domain.getComplemento());
        assertThat(entidade.getBairro()).isEqualTo(domain.getBairro());
        assertThat(entidade.getPontoReferencia()).isEqualTo(domain.getPontoReferencia());
        assertThat(entidade.getCep()).isEqualTo(domain.getCep());
        assertThat(entidade.getMunicipio()).isEqualTo(domain.getMunicipio());
        assertThat(entidade.getUf()).isEqualTo(domain.getUf());
        assertThat(entidade.getPrincipal()).isEqualTo(domain.getPrincipal());
        assertThat(entidade.getDataCriacao()).isEqualTo(domain.getDataCriacao());
        assertThat(entidade.getDataAlteracao()).isEqualTo(domain.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve retornar null ao converter Dominio nulo para Entidade")
    void deveRetornarNullQuandoDominioNulo() {
        EnderecoEntidade entidade = EnderecoMapper.toEntity(null);
        assertThat(entidade).isNull();
    }
}