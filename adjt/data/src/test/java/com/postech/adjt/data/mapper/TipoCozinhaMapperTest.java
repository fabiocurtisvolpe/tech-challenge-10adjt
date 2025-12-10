package com.postech.adjt.data.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.adjt.data.entidade.TipoCozinhaEntidade;
import com.postech.adjt.domain.entidade.TipoCozinha;

class TipoCozinhaMapperTest {

    @Test
    @DisplayName("Deve converter TipoCozinhaEntidade para TipoCozinha (Domínio) corretamente")
    void toDomain_DeveConverterEntidadeParaDominio_QuandoEntidadeValida() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();

        TipoCozinhaEntidade entidade = new TipoCozinhaEntidade();
        entidade.setId(1);
        entidade.setNome("Italiana");
        entidade.setDescricao("Massas e pizzas tradicionais");
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);
        entidade.setAtivo(true); // O mapper atual não está mapeando 'ativo', mas configuramos a entidade completa

        // Act
        TipoCozinha dominio = TipoCozinhaMapper.toDomain(entidade);

        // Assert
        assertNotNull(dominio);
        assertEquals(entidade.getId(), dominio.getId());
        assertEquals(entidade.getNome(), dominio.getNome());
        assertEquals(entidade.getDescricao(), dominio.getDescricao());
        assertEquals(entidade.getDataCriacao(), dominio.getDataCriacao());
        assertEquals(entidade.getDataAlteracao(), dominio.getDataAlteracao());
        
        // Observação: O seu mapper atual NÃO está copiando o campo 'ativo'. 
        // Se isso for intencional, ok. Se não, vale a pena ajustar o mapper e adicionar:
        // assertEquals(entidade.getAtivo(), dominio.getAtivo());
    }

    @Test
    @DisplayName("toDomain deve retornar null quando a entrada for null")
    void toDomain_DeveRetornarNull_QuandoEntidadeForNull() {
        // Act
        TipoCozinha resultado = TipoCozinhaMapper.toDomain(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve converter TipoCozinha (Domínio) para TipoCozinhaEntidade corretamente")
    void toEntity_DeveConverterDominioParaEntidade_QuandoDominioValido() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();

        TipoCozinha dominio = TipoCozinha.builder()
                .id(10)
                .nome("Japonesa")
                .descricao("Sushi e Sashimi")
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .ativo(true)
                .build();

        // Act
        TipoCozinhaEntidade entidade = TipoCozinhaMapper.toEntity(dominio);

        // Assert
        assertNotNull(entidade);
        assertEquals(dominio.getId(), entidade.getId());
        assertEquals(dominio.getNome(), entidade.getNome());
        assertEquals(dominio.getDescricao(), entidade.getDescricao());
        assertEquals(dominio.getDataCriacao(), entidade.getDataCriacao());
        assertEquals(dominio.getDataAlteracao(), entidade.getDataAlteracao());
    }

    @Test
    @DisplayName("toEntity deve retornar null quando a entrada for null")
    void toEntity_DeveRetornarNull_QuandoDominioForNull() {
        // Act
        TipoCozinhaEntidade resultado = TipoCozinhaMapper.toEntity(null);

        // Assert
        assertNull(resultado);
    }
}