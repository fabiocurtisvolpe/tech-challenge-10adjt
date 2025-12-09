package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.entidade.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CardapioValidator - Testes Unitários")
class CardapioValidatorTest {

    private Restaurante restaurante;
    private Cardapio cardapioValido;
    private Integer idUsuarioLogado;

    @BeforeEach
    void setUp() {
        TipoUsuario tipoUsuario = TipoUsuario.builder()
                .id(1)
                .nome("DONO")
                .descricao("Dono de restaurante")
                .build();

        Usuario dono = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senha123")
                .tipoUsuario(tipoUsuario)
                .isDono(true)
                .build();

        idUsuarioLogado = 1;

        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("Italiana")
                .descricao("Culinária italiana")
                .build();

        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .numero("123")
                .bairro("Centro")
                .cep("12345-678")
                .municipio("São Paulo")
                .uf("SP")
                .principal(true)
                .build();

        restaurante = Restaurante.builder()
                .nome("Restaurante Italiano")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        cardapioValido = Cardapio.builder()
                .nome("Pizza Margherita")
                .descricao("Pizza tradicional italiana")
                .preco(35.50)
                .restaurante(restaurante)
                .build();
    }

    @Test
    @DisplayName("Deve validar cardápio válido com sucesso")
    void testValidarCardapioValido() {
        // Act & Assert
        assertDoesNotThrow(() -> CardapioValidator.validar(cardapioValido, idUsuarioLogado));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar cardápio nulo")
    void testValidarCardapioNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardapioValidator.validar(null, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.CARDAPIO_NULO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar cardápio com nome nulo")
    void testValidarCardapioComNomeNulo() {
        // Arrange
        Cardapio cardapio = Cardapio.builder()
                .nome(null)
                .descricao("Descrição")
                .preco(35.50)
                .restaurante(restaurante)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardapioValidator.validar(cardapio, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.NOME_CARDAPIO_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar cardápio com nome vazio")
    void testValidarCardapioComNomeVazio() {
        // Arrange
        Cardapio cardapio = Cardapio.builder()
                .nome("  ")
                .descricao("Descrição")
                .preco(35.50)
                .restaurante(restaurante)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardapioValidator.validar(cardapio, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.NOME_CARDAPIO_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar cardápio com restaurante nulo")
    void testValidarCardapioComRestauranteNulo() {
        // Arrange
        Cardapio cardapio = Cardapio.builder()
                .nome("Pizza Margherita")
                .descricao("Descrição")
                .preco(35.50)
                .restaurante(null)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardapioValidator.validar(cardapio, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.RESTAURANTE_OBRIGATORIO_CARDAPIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar cardápio com dono do restaurante nulo")
    void testValidarCardapioComDonoRestauranteNulo() {
        // Arrange
        Restaurante restauranteSemDono = Restaurante.builder()
                .nome("Restaurante Italiano")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(restaurante.getTipoCozinha())
                .endereco(restaurante.getEndereco())
                .dono(null)
                .build();

        Cardapio cardapio = Cardapio.builder()
                .nome("Pizza Margherita")
                .descricao("Descrição")
                .preco(35.50)
                .restaurante(restauranteSemDono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardapioValidator.validar(cardapio, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.DONO_RESTAURANTE_CARDAPIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar cardápio com preço nulo")
    void testValidarCardapioComPrecoNulo() {
        // Arrange
        Cardapio cardapio = Cardapio.builder()
                .nome("Pizza Margherita")
                .descricao("Descrição")
                .preco(null)
                .restaurante(restaurante)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardapioValidator.validar(cardapio, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.PRECO_CARDAPIO_INVALIDO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar cardápio com preço zero")
    void testValidarCardapioComPrecoZero() {
        // Arrange
        Cardapio cardapio = Cardapio.builder()
                .nome("Pizza Margherita")
                .descricao("Descrição")
                .preco(0.0)
                .restaurante(restaurante)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardapioValidator.validar(cardapio, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.PRECO_CARDAPIO_INVALIDO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar cardápio com preço negativo")
    void testValidarCardapioComPrecoNegativo() {
        // Arrange
        Cardapio cardapio = Cardapio.builder()
                .nome("Pizza Margherita")
                .descricao("Descrição")
                .preco(-10.0)
                .restaurante(restaurante)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardapioValidator.validar(cardapio, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.PRECO_CARDAPIO_INVALIDO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar cardápio com preço válido (maior que zero)")
    void testValidarCardapioComPrecoValido() {
        // Arrange
        Cardapio cardapio = Cardapio.builder()
                .nome("Pizza Margherita")
                .descricao("Descrição")
                .preco(0.01)
                .restaurante(restaurante)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> CardapioValidator.validar(cardapio, idUsuarioLogado));
    }

    @Test
    @DisplayName("Deve validar cardápio com preço alto")
    void testValidarCardapioComPrecoAlto() {
        // Arrange
        Cardapio cardapio = Cardapio.builder()
                .nome("Pizza Especial")
                .descricao("Pizza gourmet")
                .preco(150.99)
                .restaurante(restaurante)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> CardapioValidator.validar(cardapio, idUsuarioLogado));
    }
}
