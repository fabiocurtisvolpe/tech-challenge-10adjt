package com.postech.adjt.data.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.postech.adjt.data.entidade.CardapioEntidade;
import com.postech.adjt.data.entidade.RestauranteEntirade;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;

class CardapioMapperTest {

    @Test
    void toDomain_DeveConverterEntidadeParaDominio_QuandoEntidadeValida() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        
        // Mock das dependências
        RestauranteEntirade restauranteEntidadeMock = mock(RestauranteEntirade.class);
        Restaurante restauranteDomainMock = mock(Restaurante.class);

        CardapioEntidade entidade = new CardapioEntidade();
        entidade.setId(1);
        entidade.setNome("Pizza Margherita");
        entidade.setDescricao("Molho, queijo e manjericão");
        entidade.setPreco(new BigDecimal("45.50"));
        entidade.setFoto("url_foto.jpg");
        entidade.setDisponivel(true);
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);
        entidade.setRestaurante(restauranteEntidadeMock);

        // Usamos try-with-resources para garantir que o Mock Static seja fechado após o teste
        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            // Definimos o comportamento do RestauranteMapper (não queremos testar ele aqui, apenas o CardapioMapper)
            restauranteMapperMock.when(() -> RestauranteMapper.toDomain(any())).thenReturn(restauranteDomainMock);

            // Act
            Cardapio dominio = CardapioMapper.toDomain(entidade);

            // Assert
            assertNotNull(dominio);
            assertEquals(entidade.getId(), dominio.getId());
            assertEquals(entidade.getNome(), dominio.getNome());
            assertEquals(entidade.getDescricao(), dominio.getDescricao());
            assertEquals(45.50, dominio.getPreco()); // Verifica conversão BigDecimal -> Double
            assertEquals(entidade.getFoto(), dominio.getFoto());
            assertEquals(entidade.getDisponivel(), dominio.getDisponivel());
            assertEquals(entidade.getDataCriacao(), dominio.getDataCriacao());
            assertEquals(entidade.getDataAlteracao(), dominio.getDataAlteracao());
            assertEquals(restauranteDomainMock, dominio.getRestaurante());
        }
    }

    @Test
    void toDomain_DeveRetornarNull_QuandoEntidadeForNull() {
        // Act
        Cardapio resultado = CardapioMapper.toDomain(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    void toDomain_DeveLidarComAtributosNulos() {
        // Arrange - Entidade com preço nulo (para testar a lógica do ternário)
        CardapioEntidade entidade = new CardapioEntidade();
        entidade.setId(1);
        entidade.setPreco(null); 
        
        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toDomain(any())).thenReturn(null);

            // Act
            Cardapio dominio = CardapioMapper.toDomain(entidade);

            // Assert
            assertNotNull(dominio);
            assertNull(dominio.getPreco()); // Deve ser null, não deve lançar NullPointerException
        }
    }

    @Test
    void toEntity_DeveConverterDominioParaEntidade_QuandoDominioValido() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        Restaurante restauranteDomainMock = mock(Restaurante.class);
        RestauranteEntirade restauranteEntidadeMock = mock(RestauranteEntirade.class);

        Cardapio dominio = Cardapio.builder()
                .id(10)
                .nome("Hambúrguer")
                .descricao("Pão, carne e queijo")
                .preco(30.00)
                .foto("burger.png")
                .disponivel(false)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .restaurante(restauranteDomainMock)
                .build();

        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toEntity(any())).thenReturn(restauranteEntidadeMock);

            // Act
            CardapioEntidade entidade = CardapioMapper.toEntity(dominio);

            // Assert
            assertNotNull(entidade);
            assertEquals(dominio.getId(), entidade.getId());
            assertEquals(dominio.getNome(), entidade.getNome());
            assertEquals(dominio.getDescricao(), entidade.getDescricao());
            assertEquals(new BigDecimal("30.0"), entidade.getPreco()); // Verifica conversão Double -> BigDecimal
            assertEquals(dominio.getFoto(), entidade.getFoto());
            assertEquals(dominio.getDisponivel(), entidade.getDisponivel());
            assertEquals(dominio.getDataCriacao(), entidade.getDataCriacao());
            assertEquals(dominio.getDataAlteracao(), entidade.getDataAlteracao());
            assertEquals(restauranteEntidadeMock, entidade.getRestaurante());
        }
    }

    @Test
    void toEntity_DeveRetornarNull_QuandoDominioForNull() {
        // Act
        CardapioEntidade resultado = CardapioMapper.toEntity(null);

        // Assert
        assertNull(resultado);
    }
    
    @Test
    void toEntity_DeveLidarComAtributosNulos() {
        // Arrange - Dominio com preço nulo
        Cardapio dominio = Cardapio.builder()
                .id(10)
                .preco(null)
                .build();

        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toEntity(any())).thenReturn(null);

            // Act
            CardapioEntidade entidade = CardapioMapper.toEntity(dominio);

            // Assert
            assertNotNull(entidade);
            assertNull(entidade.getPreco()); // Deve ser null
        }
    }
}