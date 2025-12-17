package com.postech.adjt.data.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioMapperTest {

    @Test
    @DisplayName("toDomain: Deve retornar TipoUsuarioDonoRestaurante quando isDono for true")
    void toDomain_DeveRetornarDonoRestaurante() {
    
        LocalDateTime agora = LocalDateTime.now();
        Restaurante restauranteDomain = mock(Restaurante.class);
        RestauranteEntidade restauranteEntidade = mock(RestauranteEntidade.class);

        TipoUsuarioEntidade entidade = new TipoUsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("Gerente");
        entidade.setDescricao("Gerente do Local");
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);
        entidade.setAtivo(true);
        entidade.setIsEditavel(true);
        entidade.setIsDono(true);
        entidade.setRestaurante(restauranteEntidade);

        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toDomain(restauranteEntidade))
                    .thenReturn(restauranteDomain);

            TipoUsuario domain = TipoUsuarioMapper.toDomain(entidade);

            assertNotNull(domain);
            assertInstanceOf(TipoUsuarioDonoRestaurante.class, domain);
            
            assertEquals(entidade.getId(), domain.getId());
            assertEquals(entidade.getNome(), domain.getNome());
            assertEquals(entidade.getDescricao(), domain.getDescricao());
            assertTrue(domain.getIsDono());
            assertEquals(restauranteDomain, domain.getRestaurante());
        }
    }

    @Test
    @DisplayName("toDomain: Deve retornar TipoUsuarioGenrico quando isDono for false")
    void toDomain_DeveRetornarTipoGenerico() {

        LocalDateTime agora = LocalDateTime.now();
        Restaurante restauranteDomain = mock(Restaurante.class);
        RestauranteEntidade restauranteEntidade = mock(RestauranteEntidade.class);

        TipoUsuarioEntidade entidade = new TipoUsuarioEntidade();
        entidade.setId(2);
        entidade.setNome("Garçom");
        entidade.setIsDono(false); 
        entidade.setRestaurante(restauranteEntidade);
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);

        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toDomain(restauranteEntidade))
                    .thenReturn(restauranteDomain);

            TipoUsuario domain = TipoUsuarioMapper.toDomain(entidade);

            assertNotNull(domain);
            assertInstanceOf(TipoUsuarioGenrico.class, domain);
            
            assertEquals(entidade.getId(), domain.getId());
            assertFalse(domain.getIsDono());
            assertEquals(restauranteDomain, domain.getRestaurante());
        }
    }

    @Test
    @DisplayName("toDomain: Deve retornar null quando entrada for null")
    void toDomain_DeveRetornarNull() {
        assertNull(TipoUsuarioMapper.toDomain(null));
    }

    @Test
    @DisplayName("toEntity: Deve mapear TipoUsuarioDonoRestaurante com isDono=true")
    void toEntity_DeveMapearDonoCorretamente() {

        Restaurante restauranteDomain = mock(Restaurante.class);
        RestauranteEntidade restauranteEntidade = mock(RestauranteEntidade.class);

        TipoUsuarioDonoRestaurante domain = TipoUsuarioDonoRestaurante.builder()
                .id(1)
                .nome("Dono")
                .ativo(true)
                .restaurante(restauranteDomain)
                .build();

        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toEntity(restauranteDomain))
                    .thenReturn(restauranteEntidade);

            TipoUsuarioEntidade entidade = TipoUsuarioMapper.toEntity(domain);

            assertNotNull(entidade);
            assertEquals(domain.getId(), entidade.getId());
            assertEquals(domain.getNome(), entidade.getNome());
            assertTrue(entidade.getIsDono()); 
            assertTrue(entidade.getIsEditavel());
            assertEquals(restauranteEntidade, entidade.getRestaurante());
        }
    }

    @Test
    @DisplayName("toEntity: Deve mapear TipoUsuarioGenrico com isDono=false")
    void toEntity_DeveMapearGenericoCorretamente() {

        Restaurante restauranteDomain = mock(Restaurante.class);
        RestauranteEntidade restauranteEntidade = mock(RestauranteEntidade.class);

        TipoUsuarioGenrico domain = TipoUsuarioGenrico.builder()
                .id(2)
                .nome("Funcionario")
                .ativo(false)
                .restaurante(restauranteDomain)
                .build();

        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toEntity(restauranteDomain))
                    .thenReturn(restauranteEntidade);

            TipoUsuarioEntidade entidade = TipoUsuarioMapper.toEntity(domain);

            assertNotNull(entidade);
            assertEquals(domain.getId(), entidade.getId());
            assertFalse(entidade.getIsDono());
            assertFalse(entidade.getAtivo());
            assertEquals(restauranteEntidade, entidade.getRestaurante());
        }
    }

    @Test
    @DisplayName("toEntity: Deve definir ativo como true se for nulo no domínio")
    void toEntity_DeveDefinirAtivoDefault() {
        
        TipoUsuarioGenrico domain = TipoUsuarioGenrico.builder()
                .id(3)
                .ativo(null)
                .build();

        try (MockedStatic<RestauranteMapper> restauranteMapperMock = mockStatic(RestauranteMapper.class)) {
            restauranteMapperMock.when(() -> RestauranteMapper.toEntity(any())).thenReturn(null);

            TipoUsuarioEntidade entidade = TipoUsuarioMapper.toEntity(domain);

            assertNotNull(entidade);
            assertTrue(entidade.getAtivo());
        }
    }

    @Test
    @DisplayName("toEntity: Deve retornar null quando entrada for null")
    void toEntity_DeveRetornarNull() {
        assertNull(TipoUsuarioMapper.toEntity(null));
    }
}