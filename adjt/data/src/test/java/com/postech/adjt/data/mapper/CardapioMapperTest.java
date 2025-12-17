package com.postech.adjt.data.mapper;

import com.postech.adjt.data.entidade.CardapioEntidade;
import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CardapioMapperTest {

    @Test
    @DisplayName("Deve converter CardapioEntidade para Cardapio (Domain) corretamente")
    void deveConverterEntidadeParaDominio() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        
        // Simulando a dependência do restaurante
        RestauranteEntidade restauranteEntidade = new RestauranteEntidade();
        restauranteEntidade.setId(1);
        
        Restaurante restauranteDomain = Restaurante.builder().id(1).build();

        CardapioEntidade entidade = new CardapioEntidade();
        entidade.setId(10);
        entidade.setNome("Pizza");
        entidade.setDescricao("Queijo");
        entidade.setPreco(BigDecimal.TEN);
        entidade.setFoto("foto.jpg");
        entidade.setDisponivel(true);
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);
        entidade.setRestaurante(restauranteEntidade);

        // Mockando o RestauranteMapper estático para isolar o teste (Opcional, mas recomendado se quiser teste unitário puro)
        try (MockedStatic<RestauranteMapper> restauranteMapperMock = Mockito.mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toDomain(any())).thenReturn(restauranteDomain);

            // Act
            Cardapio domain = CardapioMapper.toDomain(entidade);

            // Assert
            assertThat(domain).isNotNull();
            assertThat(domain.getId()).isEqualTo(entidade.getId());
            assertThat(domain.getNome()).isEqualTo(entidade.getNome());
            assertThat(domain.getDescricao()).isEqualTo(entidade.getDescricao());
            assertThat(domain.getPreco()).isEqualTo(entidade.getPreco());
            assertThat(domain.getFoto()).isEqualTo(entidade.getFoto());
            assertThat(domain.getDisponivel()).isEqualTo(entidade.getDisponivel());
            assertThat(domain.getDataCriacao()).isEqualTo(entidade.getDataCriacao());
            assertThat(domain.getDataAlteracao()).isEqualTo(entidade.getDataAlteracao());
            assertThat(domain.getRestaurante()).isEqualTo(restauranteDomain);
        }
    }

    @Test
    @DisplayName("Deve converter Cardapio (Domain) para CardapioEntidade corretamente")
    void deveConverterDominioParaEntidade() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        
        Restaurante restauranteDomain = Restaurante.builder().id(1).build();
        RestauranteEntidade restauranteEntidade = new RestauranteEntidade();
        restauranteEntidade.setId(1);

        Cardapio domain = Cardapio.builder()
                .id(10)
                .nome("Hambúrguer")
                .descricao("Carne")
                .preco(BigDecimal.valueOf(20.0))
                .foto("burger.jpg")
                .disponivel(false)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .restaurante(restauranteDomain)
                .build();

        try (MockedStatic<RestauranteMapper> restauranteMapperMock = Mockito.mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toEntity(any())).thenReturn(restauranteEntidade);

            // Act
            CardapioEntidade entidade = CardapioMapper.toEntity(domain);

            // Assert
            assertThat(entidade).isNotNull();
            assertThat(entidade.getId()).isEqualTo(domain.getId());
            assertThat(entidade.getNome()).isEqualTo(domain.getNome());
            assertThat(entidade.getDescricao()).isEqualTo(domain.getDescricao());
            assertThat(entidade.getPreco()).isEqualTo(domain.getPreco());
            assertThat(entidade.getFoto()).isEqualTo(domain.getFoto());
            assertThat(entidade.getDisponivel()).isEqualTo(domain.getDisponivel());
            assertThat(entidade.getDataCriacao()).isEqualTo(domain.getDataCriacao());
            assertThat(entidade.getDataAlteracao()).isEqualTo(domain.getDataAlteracao());
            assertThat(entidade.getRestaurante()).isEqualTo(restauranteEntidade);
        }
    }

    @Test
    @DisplayName("Deve retornar null quando a entrada para toDomain for nula")
    void deveRetornarNullQuandoEntidadeNula() {
        Cardapio result = CardapioMapper.toDomain(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve retornar null quando a entrada para toEntity for nula")
    void deveRetornarNullQuandoDominioNulo() {
        CardapioEntidade result = CardapioMapper.toEntity(null);
        assertThat(result).isNull();
    }
}