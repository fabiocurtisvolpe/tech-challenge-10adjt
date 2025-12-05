package com.postech.adjt.data.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.domain.entidade.TipoUsuario;

/**
 * Testes unitários para TipoUsuarioMapper
 * 
 * @author Fabio
 * @since 2025-12-05
 */
@DisplayName("TipoUsuarioMapper - Testes Unitários")
class TipoUsuarioMapperTest {

    private TipoUsuarioEntidade entidade;
    private TipoUsuario dominio;

    @BeforeEach
    void setUp() {
        entidade = new TipoUsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("Cliente");
        entidade.setDescricao("Usuário do tipo Cliente");
        entidade.setDataCriacao(LocalDateTime.now());
        entidade.setDataAlteracao(LocalDateTime.now());

        dominio = TipoUsuario.builder()
                .id(1)
                .nome("Cliente")
                .descricao("Usuário do tipo Cliente")
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve converter TipoUsuarioEntidade para TipoUsuario")
    void testToDomain() {
        // Act
        TipoUsuario resultado = TipoUsuarioMapper.toDomain(entidade);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Cliente", resultado.getNome());
        assertEquals("Usuário do tipo Cliente", resultado.getDescricao());
        assertNotNull(resultado.getDataCriacao());
        assertNotNull(resultado.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve retornar null ao converter entidade nula")
    void testToDomainComNull() {
        // Act
        TipoUsuario resultado = TipoUsuarioMapper.toDomain(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve converter TipoUsuario para TipoUsuarioEntidade")
    void testToEntity() {
        // Act
        TipoUsuarioEntidade resultado = TipoUsuarioMapper.toEntity(dominio);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Cliente", resultado.getNome());
        assertEquals("Usuário do tipo Cliente", resultado.getDescricao());
        assertNotNull(resultado.getDataCriacao());
        assertNotNull(resultado.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve retornar null ao converter domínio nulo")
    void testToEntityComNull() {
        // Act
        TipoUsuarioEntidade resultado = TipoUsuarioMapper.toEntity(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve manter todos os campos ao converter para domínio")
    void testToDomainMantemCampos() {
        // Act
        TipoUsuario resultado = TipoUsuarioMapper.toDomain(entidade);

        // Assert
        assertEquals(entidade.getId(), resultado.getId());
        assertEquals(entidade.getNome(), resultado.getNome());
        assertEquals(entidade.getDescricao(), resultado.getDescricao());
        assertEquals(entidade.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(entidade.getDataAlteracao(), resultado.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve manter todos os campos ao converter para entidade")
    void testToEntityMantemCampos() {
        // Act
        TipoUsuarioEntidade resultado = TipoUsuarioMapper.toEntity(dominio);

        // Assert
        assertEquals(dominio.getId(), resultado.getId());
        assertEquals(dominio.getNome(), resultado.getNome());
        assertEquals(dominio.getDescricao(), resultado.getDescricao());
        assertEquals(dominio.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(dominio.getDataAlteracao(), resultado.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve converter múltiplas entidades para domínio")
    void testToDomainMultiplos() {
        // Arrange
        TipoUsuarioEntidade entidade1 = new TipoUsuarioEntidade();
        entidade1.setId(1);
        entidade1.setNome("Cliente");
        entidade1.setDescricao("Tipo Cliente");

        TipoUsuarioEntidade entidade2 = new TipoUsuarioEntidade();
        entidade2.setId(2);
        entidade2.setNome("Fornecedor");
        entidade2.setDescricao("Tipo Fornecedor");

        // Act
        TipoUsuario resultado1 = TipoUsuarioMapper.toDomain(entidade1);
        TipoUsuario resultado2 = TipoUsuarioMapper.toDomain(entidade2);

        // Assert
        assertNotNull(resultado1);
        assertNotNull(resultado2);
        assertEquals("Cliente", resultado1.getNome());
        assertEquals("Fornecedor", resultado2.getNome());
        assertNotEquals(resultado1.getId(), resultado2.getId());
    }

    @Test
    @DisplayName("Deve converter entidade sem descrição")
    void testToDomainSemDescricao() {
        // Arrange
        TipoUsuarioEntidade entidadeSemDescricao = new TipoUsuarioEntidade();
        entidadeSemDescricao.setId(1);
        entidadeSemDescricao.setNome("Tipo Teste");
        entidadeSemDescricao.setDescricao(null);

        // Act
        TipoUsuario resultado = TipoUsuarioMapper.toDomain(entidadeSemDescricao);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getDescricao());
        assertEquals("Tipo Teste", resultado.getNome());
    }

    @Test
    @DisplayName("Deve converter domínio sem descrição")
    void testToEntitySemDescricao() {
        // Arrange
        TipoUsuario dominioSemDescricao = TipoUsuario.builder()
                .id(1)
                .nome("Tipo Teste")
                .descricao(null)
                .build();

        // Act
        TipoUsuarioEntidade resultado = TipoUsuarioMapper.toEntity(dominioSemDescricao);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getDescricao());
        assertEquals("Tipo Teste", resultado.getNome());
    }

    @Test
    @DisplayName("Deve manter ID ao converter para domínio")
    void testToDomainMantemId() {
        // Act
        TipoUsuario resultado = TipoUsuarioMapper.toDomain(entidade);

        // Assert
        assertEquals(entidade.getId(), resultado.getId());
    }

    @Test
    @DisplayName("Deve manter ID ao converter para entidade")
    void testToEntityMantemId() {
        // Act
        TipoUsuarioEntidade resultado = TipoUsuarioMapper.toEntity(dominio);

        // Assert
        assertEquals(dominio.getId(), resultado.getId());
    }

    @Test
    @DisplayName("Deve ser bidirecional - domínio para entidade e voltando")
    void testBidirecional() {
        // Act
        TipoUsuarioEntidade entidadeConvertida = TipoUsuarioMapper.toEntity(dominio);
        TipoUsuario dominioConvertiido = TipoUsuarioMapper.toDomain(entidadeConvertida);

        // Assert
        assertEquals(dominio.getId(), dominioConvertiido.getId());
        assertEquals(dominio.getNome(), dominioConvertiido.getNome());
        assertEquals(dominio.getDescricao(), dominioConvertiido.getDescricao());
    }

}
