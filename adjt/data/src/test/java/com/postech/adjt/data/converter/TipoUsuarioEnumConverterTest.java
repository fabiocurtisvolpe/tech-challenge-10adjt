package com.postech.adjt.data.converter;

import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoUsuarioEnumConverter - Testes Unitários")
class TipoUsuarioEnumConverterTest {

    private TipoUsuarioEnumConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TipoUsuarioEnumConverter();
    }

    @Test
    @DisplayName("Deve converter TipoUsuarioEnum.CLIENTE para string de banco de dados")
    void testConvertClienteParaBancoComSucesso() {
        // Act
        String resultado = converter.convertToDatabaseColumn(TipoUsuarioEnum.CLIENTE);

        // Assert
        assertNotNull(resultado);
        assertEquals("C", resultado);
    }

    @Test
    @DisplayName("Deve converter TipoUsuarioEnum.DONO_RESTAURANTE para string de banco de dados")
    void testConvertDonoRestauranteParaBancoComSucesso() {
        // Act
        String resultado = converter.convertToDatabaseColumn(TipoUsuarioEnum.DONO_RESTAURANTE);

        // Assert
        assertNotNull(resultado);
        assertEquals("D", resultado);
    }

    @Test
    @DisplayName("Deve converter TipoUsuarioEnum.FORNECEDOR para string de banco de dados")
    void testConvertFornecedorParaBancoComSucesso() {
        // Act
        String resultado = converter.convertToDatabaseColumn(TipoUsuarioEnum.FORNECEDOR);

        // Assert
        assertNotNull(resultado);
        assertEquals("F", resultado);
    }

    @Test
    @DisplayName("Deve converter TipoUsuarioEnum.PRESTADOR_SERVICO para string de banco de dados")
    void testConvertPrestadorServicoParaBancoComSucesso() {
        // Act
        String resultado = converter.convertToDatabaseColumn(TipoUsuarioEnum.PRESTADOR_SERVICO);

        // Assert
        assertNotNull(resultado);
        assertEquals("P", resultado);
    }

    @Test
    @DisplayName("Deve retornar null ao converter null para banco de dados")
    void testConvertNulParaBancoRetornaNull() {
        // Act
        String resultado = converter.convertToDatabaseColumn(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve converter string 'C' de banco para TipoUsuarioEnum.CLIENTE")
    void testConvertClienteDebancoComSucesso() {
        // Act
        TipoUsuarioEnum resultado = converter.convertToEntityAttribute("C");

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoUsuarioEnum.CLIENTE, resultado);
    }

    @Test
    @DisplayName("Deve converter string 'D' de banco para TipoUsuarioEnum.DONO_RESTAURANTE")
    void testConvertDonoRestauranteDebancoComSucesso() {
        // Act
        TipoUsuarioEnum resultado = converter.convertToEntityAttribute("D");

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, resultado);
    }

    @Test
    @DisplayName("Deve converter string 'F' de banco para TipoUsuarioEnum.FORNECEDOR")
    void testConvertFornecedorDebancoComSucesso() {
        // Act
        TipoUsuarioEnum resultado = converter.convertToEntityAttribute("F");

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoUsuarioEnum.FORNECEDOR, resultado);
    }

    @Test
    @DisplayName("Deve converter string 'P' de banco para TipoUsuarioEnum.PRESTADOR_SERVICO")
    void testConvertPrestadorServicoDebancoComSucesso() {
        // Act
        TipoUsuarioEnum resultado = converter.convertToEntityAttribute("P");

        // Assert
        assertNotNull(resultado);
        assertEquals(TipoUsuarioEnum.PRESTADOR_SERVICO, resultado);
    }

    @Test
    @DisplayName("Deve retornar null ao converter null de banco de dados")
    void testConvertNulDebancortornaNull() {
        // Act
        TipoUsuarioEnum resultado = converter.convertToEntityAttribute(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve converter bidirecional CLIENTE (TipoUsuarioEnum -> String -> TipoUsuarioEnum)")
    void testConvertBidirecionalCliente() {
        // Arrange
        TipoUsuarioEnum tipoOriginal = TipoUsuarioEnum.CLIENTE;

        // Act
        String stringBanco = converter.convertToDatabaseColumn(tipoOriginal);
        TipoUsuarioEnum tipoRecuperado = converter.convertToEntityAttribute(stringBanco);

        // Assert
        assertEquals(tipoOriginal, tipoRecuperado);
    }

    @Test
    @DisplayName("Deve converter bidirecional DONO_RESTAURANTE (TipoUsuarioEnum -> String -> TipoUsuarioEnum)")
    void testConvertBidirecionalDonoRestaurante() {
        // Arrange
        TipoUsuarioEnum tipoOriginal = TipoUsuarioEnum.DONO_RESTAURANTE;

        // Act
        String stringBanco = converter.convertToDatabaseColumn(tipoOriginal);
        TipoUsuarioEnum tipoRecuperado = converter.convertToEntityAttribute(stringBanco);

        // Assert
        assertEquals(tipoOriginal, tipoRecuperado);
    }

    @Test
    @DisplayName("Deve converter bidirecional FORNECEDOR (TipoUsuarioEnum -> String -> TipoUsuarioEnum)")
    void testConvertBidirecionalFornecedor() {
        // Arrange
        TipoUsuarioEnum tipoOriginal = TipoUsuarioEnum.FORNECEDOR;

        // Act
        String stringBanco = converter.convertToDatabaseColumn(tipoOriginal);
        TipoUsuarioEnum tipoRecuperado = converter.convertToEntityAttribute(stringBanco);

        // Assert
        assertEquals(tipoOriginal, tipoRecuperado);
    }

    @Test
    @DisplayName("Deve converter bidirecional PRESTADOR_SERVICO (TipoUsuarioEnum -> String -> TipoUsuarioEnum)")
    void testConvertBidirecionalPrestadorServico() {
        // Arrange
        TipoUsuarioEnum tipoOriginal = TipoUsuarioEnum.PRESTADOR_SERVICO;

        // Act
        String stringBanco = converter.convertToDatabaseColumn(tipoOriginal);
        TipoUsuarioEnum tipoRecuperado = converter.convertToEntityAttribute(stringBanco);

        // Assert
        assertEquals(tipoOriginal, tipoRecuperado);
    }

    @Test
    @DisplayName("Deve converter bidirecional null")
    void testConvertBidirecionalNull() {
        // Act
        String stringBanco = converter.convertToDatabaseColumn(null);
        TipoUsuarioEnum tipoRecuperado = converter.convertToEntityAttribute(stringBanco);

        // Assert
        assertNull(stringBanco);
        assertNull(tipoRecuperado);
    }

    @Test
    @DisplayName("Deve estar configurado para aplicação automática no JPA")
    void testConverterEstaConfiguradoComAutoApply() {
        // Assert
        assertTrue(TipoUsuarioEnumConverter.class.isAnnotationPresent(
            jakarta.persistence.Converter.class
        ));
    }

    @Test
    @DisplayName("Deve implementar AttributeConverter")
    void testConverterImplementaAttributeConverter() {
        // Assert
        assertTrue(jakarta.persistence.AttributeConverter.class.isAssignableFrom(
            TipoUsuarioEnumConverter.class
        ));
    }

    @Test
    @DisplayName("Deve manter case-sensitive ao converter para banco de dados")
    void testMantendoCaseSensitiveParaBanco() {
        // Act
        String resultado = converter.convertToDatabaseColumn(TipoUsuarioEnum.CLIENTE);

        // Assert
        assertEquals("C", resultado);
        assertNotEquals("c", resultado);
    }

    @Test
    @DisplayName("Deve converter todos os tipos de TipoUsuarioEnum para string")
    void testConvertirTodosOsTipos() {
        // Arrange
        TipoUsuarioEnum[] tipos = TipoUsuarioEnum.values();

        // Act & Assert
        for (TipoUsuarioEnum tipo : tipos) {
            String stringBanco = converter.convertToDatabaseColumn(tipo);
            assertNotNull(stringBanco);
            assertFalse(stringBanco.isEmpty());
            assertEquals(tipo.toString(), stringBanco);
        }
    }

    @Test
    @DisplayName("Deve converter strings de todos os tipos de banco para TipoUsuarioEnum")
    void testConvertirTodasAsStrings() {
        // Arrange
        String[] strings = {"C", "D", "F", "P"};

        // Act & Assert
        for (String str : strings) {
            TipoUsuarioEnum tipo = converter.convertToEntityAttribute(str);
            assertNotNull(tipo);
            assertTrue(tipo.toString().equals(str));
        }
    }

    @Test
    @DisplayName("Deve retornar string não vazia ao converter TipoUsuarioEnum válido")
    void testRetornarStringNaoVaziaParaTipoValido() {
        // Act
        String resultado = converter.convertToDatabaseColumn(TipoUsuarioEnum.CLIENTE);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertFalse(resultado.isBlank());
    }

    @Test
    @DisplayName("Deve recuperar todos os TipoUsuarioEnum das strings correspondentes")
    void testRecuperarTodosTiposDeStrings() {
        // Arrange
        String[] strings = {"C", "D", "F", "P"};

        // Act & Assert
        for (String str : strings) {
            TipoUsuarioEnum tipo = converter.convertToEntityAttribute(str);
            assertNotNull(tipo);
            
            // Verifica que o tipo convertido volta para a mesma string
            String stringRecuperada = converter.convertToDatabaseColumn(tipo);
            assertEquals(str, stringRecuperada);
        }
    }

    @Test
    @DisplayName("Deve ser reutilizável para múltiplas conversões")
    void testConverterReutilizavelParaMultiplasConversoes() {
        // Act
        String resultado1 = converter.convertToDatabaseColumn(TipoUsuarioEnum.CLIENTE);
        String resultado2 = converter.convertToDatabaseColumn(TipoUsuarioEnum.DONO_RESTAURANTE);
        String resultado3 = converter.convertToDatabaseColumn(TipoUsuarioEnum.FORNECEDOR);

        TipoUsuarioEnum tipo1 = converter.convertToEntityAttribute(resultado1);
        TipoUsuarioEnum tipo2 = converter.convertToEntityAttribute(resultado2);
        TipoUsuarioEnum tipo3 = converter.convertToEntityAttribute(resultado3);

        // Assert
        assertEquals(TipoUsuarioEnum.CLIENTE, tipo1);
        assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, tipo2);
        assertEquals(TipoUsuarioEnum.FORNECEDOR, tipo3);
    }
}
