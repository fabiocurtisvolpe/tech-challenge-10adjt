package com.postech.adjt.domain.entidade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Endereco - Testes Unitários")
class EnderecoTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Deve criar endereço com sucesso")
    void testCriarEnderecoComSucesso() {
        // Act
        Endereco endereco = new Endereco(
            "Rua A",
            "123",
            "Apt 101",
            "Bairro X",
            "Ponto Ref",
            "12345-678",
            "São Paulo",
            "SP",
            false,
            null
        );

        // Assert
        assertNotNull(endereco);
        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals("123", endereco.getNumero());
        assertEquals("Apt 101", endereco.getComplemento());
        assertEquals("Bairro X", endereco.getBairro());
        assertEquals("Ponto Ref", endereco.getPontoReferencia());
        assertEquals("12345-678", endereco.getCep());
        assertEquals("São Paulo", endereco.getMunicipio());
        assertEquals("SP", endereco.getUf());
        assertFalse(endereco.getPrincipal());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP é inválido")
    void testLancarExcecaoQuandoCepInvalido() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Endereco(
                "Rua A",
                "123",
                "Apt 101",
                "Bairro X",
                "Ponto Ref",
                "12345",
                "São Paulo",
                "SP",
                false,
                null
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP é null")
    void testLancarExcecaoQuandoCepEhNulo() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Endereco(
                "Rua A",
                "123",
                "Apt 101",
                "Bairro X",
                "Ponto Ref",
                null,
                "São Paulo",
                "SP",
                false,
                null
            )
        );
    }

    @Test
    @DisplayName("Deve aceitar CEP com hífen")
    void testAceitarCepComHifen() {
        // Act
        Endereco endereco = new Endereco(
            "Rua A",
            "123",
            "Apt 101",
            "Bairro X",
            "Ponto Ref",
            "12345-678",
            "São Paulo",
            "SP",
            false,
            null
        );

        // Assert
        assertEquals("12345-678", endereco.getCep());
    }

    @Test
    @DisplayName("Deve aceitar CEP sem hífen")
    void testAceitarCepSemHifen() {
        // Act
        Endereco endereco = new Endereco(
            "Rua A",
            "123",
            "Apt 101",
            "Bairro X",
            "Ponto Ref",
            "12345678",
            "São Paulo",
            "SP",
            false,
            null
        );

        // Assert
        assertEquals("12345678", endereco.getCep());
    }

    @Test
    @DisplayName("Deve criar endereço como principal")
    void testCriarEnderecoComoPrincipal() {
        // Act
        Endereco endereco = new Endereco(
            "Rua Principal",
            "100",
            "Casa 1",
            "Bairro Y",
            "Frente",
            "23456-789",
            "Rio de Janeiro",
            "RJ",
            true,
            null
        );

        // Assert
        assertTrue(endereco.getPrincipal());
    }

    @Test
    @DisplayName("Deve retornar todos os atributos corretamente")
    void testRetornarTodosAtributosCorretamente() {
        // Arrange
        String logradouro = "Avenida Brasil";
        String numero = "456";
        String complemento = "Apt 202";
        String bairro = "Centro";
        String pontoRef = "Próximo à estação";
        String cep = "34567-890";
        String municipio = "Brasília";
        String uf = "DF";

        // Act
        Endereco endereco = new Endereco(
            logradouro,
            numero,
            complemento,
            bairro,
            pontoRef,
            cep,
            municipio,
            uf,
            false,
            null
        );

        // Assert
        assertEquals(logradouro, endereco.getLogradouro());
        assertEquals(numero, endereco.getNumero());
        assertEquals(complemento, endereco.getComplemento());
        assertEquals(bairro, endereco.getBairro());
        assertEquals(pontoRef, endereco.getPontoReferencia());
        assertEquals(cep, endereco.getCep());
        assertEquals(municipio, endereco.getMunicipio());
        assertEquals(uf, endereco.getUf());
    }

    @Test
    @DisplayName("Deve criar endereço com construtor completo (com ID)")
    void testCriarEnderecoComConstrutorCompleto() {
        // Act
        Endereco endereco = new Endereco(
            1,
            "Rua C",
            "789",
            "Apt 303",
            "Bairro Z",
            "Ponto Ref",
            "45678-901",
            "Salvador",
            "BA",
            true,
            true,
            null
        );

        // Assert
        assertNotNull(endereco);
        assertEquals(Integer.valueOf(1), endereco.getId());
        assertEquals("Rua C", endereco.getLogradouro());
        assertEquals("789", endereco.getNumero());
        assertTrue(endereco.getPrincipal());
        assertTrue(endereco.getAtivo());
    }

    @Test
    @DisplayName("Deve validar CEP com apenas números")
    void testValidarCepComApenasNumeros() {
        // Act
        Endereco endereco = new Endereco(
            "Rua D",
            "321",
            "Apt 404",
            "Bairro W",
            "Ponto Ref",
            "56789012",
            "Recife",
            "PE",
            false,
            null
        );

        // Assert
        assertEquals("56789012", endereco.getCep());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP tem formato inválido com letras")
    void testLancarExcecaoQuandoCepTemLetras() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Endereco(
                "Rua E",
                "654",
                "Apt 505",
                "Bairro V",
                "Ponto Ref",
                "1234A-678",
                "Manaus",
                "AM",
                false,
                null
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP tem apenas 4 dígitos")
    void testLancarExcecaoQuandoCepMuitoCurto() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Endereco(
                "Rua F",
                "987",
                "Apt 606",
                "Bairro U",
                "Ponto Ref",
                "1234",
                "Fortaleza",
                "CE",
                false,
                null
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP tem mais de 9 dígitos")
    void testLancarExcecaoQuandoCepMuitoLongo() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Endereco(
                "Rua G",
                "147",
                "Apt 707",
                "Bairro T",
                "Ponto Ref",
                "123456-78901",
                "Curitiba",
                "PR",
                false,
                null
            )
        );
    }

    @Test
    @DisplayName("Deve criar múltiplos endereços independentemente")
    void testCriarMultiplosEnderecosIndependentemente() {
        // Act
        Endereco endereco1 = new Endereco(
            "Rua H",
            "111",
            "Apt 101",
            "Bairro A",
            "Ponto Ref",
            "11111-111",
            "São Paulo",
            "SP",
            true,
            null
        );

        Endereco endereco2 = new Endereco(
            "Rua I",
            "222",
            "Apt 202",
            "Bairro B",
            "Ponto Ref",
            "22222-222",
            "Rio de Janeiro",
            "RJ",
            false,
            null
        );

        // Assert
        assertEquals("Rua H", endereco1.getLogradouro());
        assertEquals("Rua I", endereco2.getLogradouro());
        assertTrue(endereco1.getPrincipal());
        assertFalse(endereco2.getPrincipal());
    }

    @Test
    @DisplayName("Deve retornar null para usuário quando não atribuído")
    void testRetornarNullParaUsuario() {
        // Act
        Endereco endereco = new Endereco(
            "Rua J",
            "333",
            "Apt 303",
            "Bairro C",
            "Ponto Ref",
            "33333-333",
            "Brasília",
            "DF",
            false,
            null
        );

        // Assert
        assertNull(endereco.getUsuario());
    }

    @Test
    @DisplayName("Deve aceitar complemento vazio")
    void testAceitarComplementoVazio() {
        // Act
        Endereco endereco = new Endereco(
            "Rua K",
            "444",
            "",
            "Bairro D",
            "Ponto Ref",
            "44444-444",
            "Salvador",
            "BA",
            false,
            null
        );

        // Assert
        assertEquals("", endereco.getComplemento());
    }

    @Test
    @DisplayName("Deve aceitar ponto de referência vazio")
    void testAceitarPontoReferenciavazio() {
        // Act
        Endereco endereco = new Endereco(
            "Rua L",
            "555",
            "Apt 404",
            "Bairro E",
            "",
            "55555-555",
            "Recife",
            "PE",
            false,
            null
        );

        // Assert
        assertEquals("", endereco.getPontoReferencia());
    }

    @Test
    @DisplayName("Deve validar CEP padrão brasileiro")
    void testValidarCepPadraBrasileiro() {
        // Act
        Endereco endereco = new Endereco(
            "Rua M",
            "666",
            "Apt 505",
            "Bairro F",
            "Ponto Ref",
            "01310-100",
            "São Paulo",
            "SP",
            false,
            null
        );

        // Assert
        assertEquals("01310-100", endereco.getCep());
    }

    @Test
    @DisplayName("Deve manter estado do endereço após criação")
    void testMantendoEstadoDoEndereco() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua N",
            "777",
            "Apt 606",
            "Bairro G",
            "Ponto Ref",
            "66666-666",
            "Manaus",
            "AM",
            true,
            null
        );

        // Act & Assert
        assertEquals("Rua N", endereco.getLogradouro());
        assertEquals("777", endereco.getNumero());
        assertEquals("Apt 606", endereco.getComplemento());
        assertEquals("Bairro G", endereco.getBairro());
        assertEquals("Ponto Ref", endereco.getPontoReferencia());
        assertEquals("66666-666", endereco.getCep());
        assertEquals("Manaus", endereco.getMunicipio());
        assertEquals("AM", endereco.getUf());
        assertTrue(endereco.getPrincipal());
    }

    @Test
    @DisplayName("Deve criar endereço com todos os campos preenchidos")
    void testCriarEnderecoComTodosCamposPreenchidos() {
        // Act
        Endereco endereco = new Endereco(
            2,
            "Rua O",
            "888",
            "Apt 707",
            "Bairro H",
            "Ponto Ref",
            "77777-777",
            "Fortaleza",
            "CE",
            false,
            true,
            null
        );

        // Assert
        assertNotNull(endereco.getId());
        assertNotNull(endereco.getLogradouro());
        assertNotNull(endereco.getNumero());
        assertNotNull(endereco.getComplemento());
        assertNotNull(endereco.getBairro());
        assertNotNull(endereco.getPontoReferencia());
        assertNotNull(endereco.getCep());
        assertNotNull(endereco.getMunicipio());
        assertNotNull(endereco.getUf());
        assertFalse(endereco.getPrincipal());
        assertTrue(endereco.getAtivo());
    }
}
