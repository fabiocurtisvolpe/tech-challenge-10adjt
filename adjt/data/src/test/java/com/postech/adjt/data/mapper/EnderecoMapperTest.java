package com.postech.adjt.data.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.domain.entidade.Endereco;

class EnderecoMapperTest {

    @Test
    @DisplayName("Deve converter EnderecoEntidade para Endereco (Domínio) corretamente")
    void toDomain_DeveConverterEntidadeParaDominio_QuandoEntidadeValida() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();

        EnderecoEntidade entidade = new EnderecoEntidade();
        entidade.setId(1);
        entidade.setLogradouro("Avenida Paulista");
        entidade.setNumero("1000");
        entidade.setComplemento("Apto 101");
        entidade.setBairro("Bela Vista");
        entidade.setPontoReferencia("Perto do MASP");
        entidade.setCep("01310-100");
        entidade.setMunicipio("São Paulo");
        entidade.setUf("SP");
        entidade.setPrincipal(true);
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);
        // Nota: O Mapper atual ignora o campo 'usuario', então não precisamos setá-lo aqui.

        // Act
        Endereco dominio = EnderecoMapper.toDomain(entidade);

        // Assert
        assertNotNull(dominio);
        assertEquals(entidade.getId(), dominio.getId());
        assertEquals(entidade.getLogradouro(), dominio.getLogradouro());
        assertEquals(entidade.getNumero(), dominio.getNumero());
        assertEquals(entidade.getComplemento(), dominio.getComplemento());
        assertEquals(entidade.getBairro(), dominio.getBairro());
        assertEquals(entidade.getPontoReferencia(), dominio.getPontoReferencia());
        assertEquals(entidade.getCep(), dominio.getCep());
        assertEquals(entidade.getMunicipio(), dominio.getMunicipio());
        assertEquals(entidade.getUf(), dominio.getUf());
        assertEquals(entidade.getPrincipal(), dominio.getPrincipal());
        assertEquals(entidade.getDataCriacao(), dominio.getDataCriacao());
        assertEquals(entidade.getDataAlteracao(), dominio.getDataAlteracao());
    }

    @Test
    @DisplayName("toDomain deve retornar null quando a entrada for null")
    void toDomain_DeveRetornarNull_QuandoEntidadeForNull() {
        // Act
        Endereco resultado = EnderecoMapper.toDomain(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve converter Endereco (Domínio) para EnderecoEntidade corretamente")
    void toEntity_DeveConverterDominioParaEntidade_QuandoDominioValido() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();

        Endereco dominio = Endereco.builder()
                .id(10)
                .logradouro("Rua das Flores")
                .numero("50")
                .complemento(null) // Testando campo opcional
                .bairro("Jardim")
                .pontoReferencia(null)
                .cep("12345-678")
                .municipio("Campinas")
                .uf("SP")
                .principal(false)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        // Act
        EnderecoEntidade entidade = EnderecoMapper.toEntity(dominio);

        // Assert
        assertNotNull(entidade);
        assertEquals(dominio.getId(), entidade.getId());
        assertEquals(dominio.getLogradouro(), entidade.getLogradouro());
        assertEquals(dominio.getNumero(), entidade.getNumero());
        assertNull(entidade.getComplemento()); // Verifica se manteve o null
        assertEquals(dominio.getBairro(), entidade.getBairro());
        assertNull(entidade.getPontoReferencia());
        assertEquals(dominio.getCep(), entidade.getCep());
        assertEquals(dominio.getMunicipio(), entidade.getMunicipio());
        assertEquals(dominio.getUf(), entidade.getUf());
        assertEquals(dominio.getPrincipal(), entidade.getPrincipal());
        assertEquals(dominio.getDataCriacao(), entidade.getDataCriacao());
        assertEquals(dominio.getDataAlteracao(), entidade.getDataAlteracao());
    }

    @Test
    @DisplayName("toEntity deve retornar null quando a entrada for null")
    void toEntity_DeveRetornarNull_QuandoDominioForNull() {
        // Act
        EnderecoEntidade resultado = EnderecoMapper.toEntity(null);

        // Assert
        assertNull(resultado);
    }
}