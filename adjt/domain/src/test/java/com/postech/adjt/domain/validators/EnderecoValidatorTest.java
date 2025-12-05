package com.postech.adjt.domain.validators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários para EnderecoValidator
 * 
 * Testa a validação de CEP conforme especificações de formato
 * 
 * @author Fabio
 * @since 2025-12-05
 */
@DisplayName("EnderecoValidator - Testes Unitários")
class EnderecoValidatorTest {

    @Test
    @DisplayName("Deve validar CEP com formato correto (12345-678)")
    void testValidarCepComFormatoCorreto() {
        // Arrange
        String cepValido = "12345-678";

        // Act & Assert - não deve lançar exceção
        assertDoesNotThrow(() -> EnderecoValidator.validarCep(cepValido));
    }

    @Test
    @DisplayName("Deve validar CEP sem hífen (12345678)")
    void testValidarCepSemHifen() {
        // Arrange
        String cepValido = "12345678";

        // Act & Assert - não deve lançar exceção
        assertDoesNotThrow(() -> EnderecoValidator.validarCep(cepValido));
    }

    @Test
    @DisplayName("Deve rejeitar CEP nulo")
    void testValidarCepNulo() {
        // Arrange
        String cepNulo = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EnderecoValidator.validarCep(cepNulo);
        });
    }

    @Test
    @DisplayName("Deve rejeitar CEP vazio")
    void testValidarCepVazio() {
        // Arrange
        String cepVazio = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EnderecoValidator.validarCep(cepVazio);
        });
    }

    @Test
    @DisplayName("Deve rejeitar CEP com menos de 8 dígitos")
    void testValidarCepMenoDe8Digitos() {
        // Arrange
        String cepInvalido = "1234-678";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EnderecoValidator.validarCep(cepInvalido);
        });
    }

    @Test
    @DisplayName("Deve rejeitar CEP com mais de 8 dígitos")
    void testValidarCepMaisDe8Digitos() {
        // Arrange
        String cepInvalido = "123456-789";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EnderecoValidator.validarCep(cepInvalido);
        });
    }

    @Test
    @DisplayName("Deve rejeitar CEP com caracteres não numéricos")
    void testValidarCepComCaracteresNaoNumericos() {
        // Arrange
        String cepInvalido = "ABCDE-FGH";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EnderecoValidator.validarCep(cepInvalido);
        });
    }

    @Test
    @DisplayName("Deve rejeitar CEP com espaços")
    void testValidarCepComEspacos() {
        // Arrange
        String cepInvalido = "12345 678";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EnderecoValidator.validarCep(cepInvalido);
        });
    }

    @Test
    @DisplayName("Deve rejeitar CEP com múltiplos hífens")
    void testValidarCepComMultiplosHifens() {
        // Arrange
        String cepInvalido = "123-45-678";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EnderecoValidator.validarCep(cepInvalido);
        });
    }

    @Test
    @DisplayName("Deve rejeitar CEP com hífen em posição incorreta")
    void testValidarCepComHifenEmPosicaoIncorreta() {
        // Arrange
        String cepInvalido = "1234-5678";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EnderecoValidator.validarCep(cepInvalido);
        });
    }

    @Test
    @DisplayName("Deve validar vários CEPs válidos")
    void testValidarMultiplosCepsValidos() {
        // Arrange
        String[] cepsValidos = {
            "01310-100",  // São Paulo
            "20040020",   // Rio de Janeiro
            "30140071",   // Belo Horizonte
            "88015-100",  // Florianópolis
            "70040902",   // Brasília
        };

        // Act & Assert - todos devem ser válidos
        for (String cep : cepsValidos) {
            assertDoesNotThrow(() -> EnderecoValidator.validarCep(cep),
                "CEP " + cep + " deveria ser válido");
        }
    }

}
