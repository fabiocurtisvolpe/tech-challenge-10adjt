package com.postech.adjt.domain.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;

@DisplayName("TipoCozinhaValidator - Testes Unitários")
class TipoCozinhaValidatorTest {

    private TipoCozinha tipoCozinhaValido;

    @BeforeEach
    void setUp() {
    
        tipoCozinhaValido = TipoCozinha.builder()
                .id(1)
                .nome("Italiana")
                .descricao("Culinária italiana tradicional")
                .build();
    }

    @Test
    @DisplayName("Deve validar TipoCozinha válido com sucesso")
    void testValidarTipoCozinhaValido() {
        // Act & Assert
        assertDoesNotThrow(() -> TipoCozinhaValidator.validar(tipoCozinhaValido));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoCozinha nulo")
    void testValidarTipoCozinhaNulo() {
        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoCozinhaValidator.validar(null);
        });
        assertEquals(MensagemUtil.TIPO_COZINHA_NULO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoCozinha com nome nulo")
    void testValidarTipoCozinhaComNomeNulo() {
        // Arrange
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome(null)
                .descricao("Descrição")
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoCozinhaValidator.validar(tipoCozinha);
        });
        assertEquals(MensagemUtil.TIPO_COZINHA_NULO_VALIDACAO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoCozinha com nome vazio")
    void testValidarTipoCozinhaComNomeVazio() {
        // Arrange
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("  ")
                .descricao("Descrição")
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoCozinhaValidator.validar(tipoCozinha);
        });
        assertEquals(MensagemUtil.TIPO_COZINHA_NULO_VALIDACAO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoCozinha com nome menor que 3 caracteres")
    void testValidarTipoCozinhaComNomeMuitoCurto() {
        // Arrange
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("AB")
                .descricao("Descrição")
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoCozinhaValidator.validar(tipoCozinha);
        });
        assertEquals(MensagemUtil.NOME_MINIMO_CARACTERES, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoCozinha com nome maior que 50 caracteres")
    void testValidarTipoCozinhaComNomeMuitoLongo() {
        // Arrange
        String nomeLongo = "A".repeat(51);
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome(nomeLongo)
                .descricao("Descrição")
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoCozinhaValidator.validar(tipoCozinha);
        });
        assertEquals(MensagemUtil.NOME_MAXIMO_CARACTERES, exception.getMessage());
    }


    @Test
    @DisplayName("Deve lançar exceção ao validar TipoCozinha com descrição maior que 1000 caracteres")
    void testValidarTipoCozinhaComDescricaoMuitoLonga() {
        // Arrange
        String descricaoLonga = "A".repeat(1001);
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("Italiana")
                .descricao(descricaoLonga)
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoCozinhaValidator.validar(tipoCozinha);
        });
        assertEquals(MensagemUtil.DESCRICAO_MAXIMO_CARACTERES, exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar TipoCozinha com nome exatamente 3 caracteres")
    void testValidarTipoCozinhaComNomeExatamente3Caracteres() {
        // Arrange
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("ABC")
                .descricao("Descrição")
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoCozinhaValidator.validar(tipoCozinha));
    }

    @Test
    @DisplayName("Deve validar TipoCozinha com nome exatamente 50 caracteres")
    void testValidarTipoCozinhaComNomeExatamente50Caracteres() {
        // Arrange
        String nome50 = "A".repeat(50);
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome(nome50)
                .descricao("Descrição")
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoCozinhaValidator.validar(tipoCozinha));
    }

    @Test
    @DisplayName("Deve validar TipoCozinha com descrição exatamente 1000 caracteres")
    void testValidarTipoCozinhaComDescricaoExatamente200Caracteres() {
        // Arrange
        String descricao1000 = "A".repeat(1000);
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("Italiana")
                .descricao(descricao1000)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoCozinhaValidator.validar(tipoCozinha));
    }

    @Test
    @DisplayName("Deve validar TipoCozinha sem descrição (null)")
    void testValidarTipoCozinhaSemDescricao() {
        // Arrange
        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("Italiana")
                .descricao(null)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoCozinhaValidator.validar(tipoCozinha));
    }
}
