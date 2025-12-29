package com.postech.adjt.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.api.dto.CardapioRespostaDTO;
import com.postech.adjt.api.dto.RestauranteRespostaDTO;
import com.postech.adjt.api.payload.cardapio.AtualizaCardapioPayLoad;
import com.postech.adjt.api.payload.cardapio.NovoCardapioPayLoad;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;

@ExtendWith(MockitoExtension.class)
class CardapioMapperApiTest {

    @Test
    @DisplayName("Deve converter NovoCardapioPayLoad para CardapioDTO corretamente")
    void testToNovoCardapioDTO() {
        NovoCardapioPayLoad payload = new NovoCardapioPayLoad();
        payload.setNome("X-Burger");
        payload.setDescricao("Pão, carne e queijo");
        payload.setPreco(BigDecimal.valueOf(25.0));
        payload.setFoto("url_foto");
        payload.setDisponivel(true);
        payload.setIdRestaurante(10);

        String usuarioLogado = "user@test.com";

        CardapioDTO result = CardapioMapperApi.toNovoCardapioDTO(payload, usuarioLogado);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals("X-Burger", result.nome());
        assertEquals("Pão, carne e queijo", result.descricao());
        assertEquals(BigDecimal.valueOf(25.0), result.preco());
        assertEquals("url_foto", result.foto());
        assertTrue(result.disponivel());

        assertNotNull(result.restaurante());
        assertEquals(10, result.restaurante().id());

        assertNotNull(result.restaurante().dono());
        assertEquals(usuarioLogado, result.restaurante().dono().email());
    }

    @Test
    @DisplayName("Deve converter AtualizaCardapioPayLoad para CardapioDTO corretamente")
    void testToAtualizaCardapioDTO() {
        AtualizaCardapioPayLoad payload = new AtualizaCardapioPayLoad();
        payload.setId(1);
        payload.setNome("X-Salada");
        payload.setDescricao("Com salada");
        payload.setPreco(BigDecimal.valueOf(30.0));
        payload.setFoto("url_foto_2");
        payload.setDisponivel(false);
        payload.setIdRestaurante(20);

        String usuarioLogado = "admin@test.com";

        CardapioDTO result = CardapioMapperApi.toAtualizaCardapioDTO(payload, usuarioLogado);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("X-Salada", result.nome());
        assertEquals("Com salada", result.descricao());
        assertEquals(BigDecimal.valueOf(30.0), result.preco());
        assertEquals("url_foto_2", result.foto());
        assertEquals(false, result.disponivel());

        assertNotNull(result.restaurante());
        assertEquals(20, result.restaurante().id());

        assertNotNull(result.restaurante().dono());
        assertEquals(usuarioLogado, result.restaurante().dono().email());
    }

    @Test
    @DisplayName("Deve converter Cardapio entidade para CardapioRespostaDTO corretamente")
    void testToCardapioRespostaDTO() {
        LocalDateTime agora = LocalDateTime.now();
        Restaurante restaurante = mock(Restaurante.class);

        Cardapio cardapio = Cardapio.builder()
                .id(5)
                .nome("Pizza")
                .descricao("Mussarela")
                .dataAlteracao(agora)
                .preco(BigDecimal.valueOf(50.0))
                .foto("pizza.jpg")
                .disponivel(true)
                .ativo(true)
                .restaurante(restaurante)
                .build();

        RestauranteRespostaDTO restauranteDTO = mock(RestauranteRespostaDTO.class);

        try (MockedStatic<RestauranteMapperApi> restauranteMapperMock = mockStatic(RestauranteMapperApi.class)) {
            restauranteMapperMock.when(() -> RestauranteMapperApi.toRestauranteRespostaGeralDTO(restaurante))
                    .thenReturn(restauranteDTO);

            CardapioRespostaDTO result = CardapioMapperApi.toCardapioRespostaDTO(cardapio);

            assertNotNull(result);
            assertEquals(5, result.getId());
            assertEquals("Pizza", result.getNome());
            assertEquals("Mussarela", result.getDescricao());
            assertEquals(agora, result.getDataAlteracao());
            assertEquals(BigDecimal.valueOf(50.0), result.getPreco());
            assertEquals("pizza.jpg", result.getFoto());
            assertTrue(result.getDisponivel());
            assertTrue(result.getAtivo());
            assertEquals(restauranteDTO, result.getRestaurante());
        }
    }

    @Test
    @DisplayName("Deve retornar null quando a entrada para toCardapioRespostaDTO for nula")
    void testToCardapioRespostaDTO_NullInput() {
        CardapioRespostaDTO result = CardapioMapperApi.toCardapioRespostaDTO(null);
        assertNull(result);
    }
}