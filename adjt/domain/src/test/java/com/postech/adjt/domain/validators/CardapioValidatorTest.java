package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardapioValidatorTest {

    @Mock
    private Cardapio cardapioMock;

    @Mock
    private Restaurante restauranteMock;

    @Mock
    private Usuario donoMock;

    private static final Integer ID_USUARIO_LOGADO = 1;

    @Test
    @DisplayName("Deve validar cardápio com sucesso")
    void deveValidarComSucesso() {
        when(cardapioMock.getNome()).thenReturn("Pizza Margherita");
        when(cardapioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(donoMock);
        when(donoMock.getId()).thenReturn(ID_USUARIO_LOGADO);
        when(cardapioMock.getPreco()).thenReturn(new BigDecimal("50.00"));

        assertThatCode(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve falhar quando cardápio for nulo")
    void deveFalharCardapioNulo() {
        assertThatThrownBy(() -> CardapioValidator.validar(null, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando nome for nulo")
    void deveFalharNomeNulo() {
        when(cardapioMock.getNome()).thenReturn(null);

        assertThatThrownBy(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando nome for vazio")
    void deveFalharNomeVazio() {
        when(cardapioMock.getNome()).thenReturn("");

        assertThatThrownBy(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando restaurante for nulo")
    void deveFalharRestauranteNulo() {
        when(cardapioMock.getNome()).thenReturn("Pizza");
        when(cardapioMock.getRestaurante()).thenReturn(null);

        assertThatThrownBy(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando dono do restaurante for nulo")
    void deveFalharDonoNulo() {
        when(cardapioMock.getNome()).thenReturn("Pizza");
        when(cardapioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(null);

        assertThatThrownBy(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não for o dono")
    void deveFalharUsuarioNaoDono() {
        when(cardapioMock.getNome()).thenReturn("Pizza");
        when(cardapioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(donoMock);
        when(donoMock.getId()).thenReturn(999); 

        assertThatThrownBy(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando preço for nulo")
    void deveFalharPrecoNulo() {
        when(cardapioMock.getNome()).thenReturn("Pizza");
        when(cardapioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(donoMock);
        when(donoMock.getId()).thenReturn(ID_USUARIO_LOGADO);
        when(cardapioMock.getPreco()).thenReturn(null);

        assertThatThrownBy(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando preço for zero")
    void deveFalharPrecoZero() {
        when(cardapioMock.getNome()).thenReturn("Pizza");
        when(cardapioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(donoMock);
        when(donoMock.getId()).thenReturn(ID_USUARIO_LOGADO);
        when(cardapioMock.getPreco()).thenReturn(BigDecimal.ZERO);

        assertThatThrownBy(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando preço for negativo")
    void deveFalharPrecoNegativo() {
        when(cardapioMock.getNome()).thenReturn("Pizza");
        when(cardapioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(donoMock);
        when(donoMock.getId()).thenReturn(ID_USUARIO_LOGADO);
        when(cardapioMock.getPreco()).thenReturn(new BigDecimal("-10.00"));

        assertThatThrownBy(() -> CardapioValidator.validar(cardapioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(IllegalArgumentException.class);
    }
}