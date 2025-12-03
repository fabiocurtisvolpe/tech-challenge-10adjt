package com.postech.adjt.domain.entidade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Base - Testes Unitários")
class BaseTest {

    private Base base;

    @BeforeEach
    void setUp() {
        base = new Base();
    }

    @Test
    @DisplayName("Deve inicializar Base com valores padrão")
    void testInicializarBaseComValoresPadrao() {
        // Act & Assert
        assertNull(base.getId());
        assertNull(base.getDataCriacao());
        assertNull(base.getDataAlteracao());
        assertNull(base.getAtivo());
    }

    @Test
    @DisplayName("Deve definir e retornar ID corretamente")
    void testDefinirERetornarId() {
        // Act
        base.setId(1);

        // Assert
        assertEquals(Integer.valueOf(1), base.getId());
    }

    @Test
    @DisplayName("Deve definir e retornar ID com valor 0")
    void testDefinirIdComValorZero() {
        // Act
        base.setId(0);

        // Assert
        assertEquals(Integer.valueOf(0), base.getId());
    }

    @Test
    @DisplayName("Deve definir e retornar ID com valor negativo")
    void testDefinirIdComValorNegativo() {
        // Act
        base.setId(-1);

        // Assert
        assertEquals(Integer.valueOf(-1), base.getId());
    }

    @Test
    @DisplayName("Deve definir e retornar ID com valor grande")
    void testDefinirIdComValorGrande() {
        // Act
        base.setId(Integer.MAX_VALUE);

        // Assert
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), base.getId());
    }

    @Test
    @DisplayName("Deve substituir ID anterior")
    void testSubstituirIdAnterior() {
        // Arrange
        base.setId(1);

        // Act
        base.setId(2);

        // Assert
        assertEquals(Integer.valueOf(2), base.getId());
    }

    @Test
    @DisplayName("Deve definir ID para null")
    void testDefinirIdParaNull() {
        // Arrange
        base.setId(1);

        // Act
        base.setId(null);

        // Assert
        assertNull(base.getId());
    }

    @Test
    @DisplayName("Deve definir e retornar dataCriacao corretamente")
    void testDefinirERetornarDataCriacao() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();

        // Act
        base.setDataCriacao(agora);

        // Assert
        assertEquals(agora, base.getDataCriacao());
    }

    @Test
    @DisplayName("Deve definir dataCriacao com data no passado")
    void testDefinirDataCriacao() {
        // Arrange
        LocalDateTime dataPassada = LocalDateTime.of(2020, 1, 1, 10, 30, 0);

        // Act
        base.setDataCriacao(dataPassada);

        // Assert
        assertEquals(dataPassada, base.getDataCriacao());
    }

    @Test
    @DisplayName("Deve definir dataCriacao com data no futuro")
    void testDefinirDataCriaoComDataNoFuturo() {
        // Arrange
        LocalDateTime dataFutura = LocalDateTime.of(2030, 12, 31, 23, 59, 59);

        // Act
        base.setDataCriacao(dataFutura);

        // Assert
        assertEquals(dataFutura, base.getDataCriacao());
    }

    @Test
    @DisplayName("Deve substituir dataCriacao anterior")
    void testSubstituirDataCriacao() {
        // Arrange
        LocalDateTime data1 = LocalDateTime.of(2020, 1, 1, 10, 0, 0);
        LocalDateTime data2 = LocalDateTime.of(2021, 6, 15, 15, 30, 0);

        // Act
        base.setDataCriacao(data1);
        base.setDataCriacao(data2);

        // Assert
        assertEquals(data2, base.getDataCriacao());
    }

    @Test
    @DisplayName("Deve definir dataCriacao para null")
    void testDefinirDataCriaoParaNull() {
        // Arrange
        LocalDateTime data = LocalDateTime.now();
        base.setDataCriacao(data);

        // Act
        base.setDataCriacao(null);

        // Assert
        assertNull(base.getDataCriacao());
    }

    @Test
    @DisplayName("Deve definir e retornar dataAlteracao corretamente")
    void testDefinirERetornarDataAlteracao() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();

        // Act
        base.setDataAlteracao(agora);

        // Assert
        assertEquals(agora, base.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve definir dataAlteracao com data específica")
    void testDefinirDataAlteracao() {
        // Arrange
        LocalDateTime dataAlteracao = LocalDateTime.of(2022, 5, 10, 14, 45, 30);

        // Act
        base.setDataAlteracao(dataAlteracao);

        // Assert
        assertEquals(dataAlteracao, base.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve substituir dataAlteracao anterior")
    void testSubstituirDataAlteracao() {
        // Arrange
        LocalDateTime data1 = LocalDateTime.of(2022, 1, 1, 10, 0, 0);
        LocalDateTime data2 = LocalDateTime.of(2023, 12, 31, 23, 59, 59);

        // Act
        base.setDataAlteracao(data1);
        base.setDataAlteracao(data2);

        // Assert
        assertEquals(data2, base.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve definir dataAlteracao para null")
    void testDefinirDataAlteracaoParaNull() {
        // Arrange
        LocalDateTime data = LocalDateTime.now();
        base.setDataAlteracao(data);

        // Act
        base.setDataAlteracao(null);

        // Assert
        assertNull(base.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve definir e retornar ativo como true")
    void testDefinirERetornarAtivoTrue() {
        // Act
        base.setAtivo(true);

        // Assert
        assertTrue(base.getAtivo());
    }

    @Test
    @DisplayName("Deve definir e retornar ativo como false")
    void testDefinirERetornarAtivoFalse() {
        // Act
        base.setAtivo(false);

        // Assert
        assertFalse(base.getAtivo());
    }

    @Test
    @DisplayName("Deve substituir ativo anterior")
    void testSubstituirAtivoAnterior() {
        // Arrange
        base.setAtivo(true);

        // Act
        base.setAtivo(false);

        // Assert
        assertFalse(base.getAtivo());
    }

    @Test
    @DisplayName("Deve alternar ativo múltiplas vezes")
    void testAlternarAtivoMultiplasTimes() {
        // Act
        base.setAtivo(true);
        assertTrue(base.getAtivo());

        base.setAtivo(false);
        assertFalse(base.getAtivo());

        base.setAtivo(true);
        assertTrue(base.getAtivo());

        // Assert
        assertTrue(base.getAtivo());
    }

    @Test
    @DisplayName("Deve definir ativo para null")
    void testDefinirAtivoParaNull() {
        // Arrange
        base.setAtivo(true);

        // Act
        base.setAtivo(null);

        // Assert
        assertNull(base.getAtivo());
    }

    @Test
    @DisplayName("Deve definir todos os atributos simultaneamente")
    void testDefinirTodosAtributosSimultaneamente() {
        // Arrange
        Integer id = 5;
        LocalDateTime dataCriacao = LocalDateTime.of(2023, 1, 15, 10, 30, 0);
        LocalDateTime dataAlteracao = LocalDateTime.of(2023, 6, 20, 14, 45, 30);
        Boolean ativo = true;

        // Act
        base.setId(id);
        base.setDataCriacao(dataCriacao);
        base.setDataAlteracao(dataAlteracao);
        base.setAtivo(ativo);

        // Assert
        assertEquals(id, base.getId());
        assertEquals(dataCriacao, base.getDataCriacao());
        assertEquals(dataAlteracao, base.getDataAlteracao());
        assertEquals(ativo, base.getAtivo());
    }

    @Test
    @DisplayName("Deve independentizar múltiplas instâncias de Base")
    void testIndependentizarMultiplasInstancias() {
        // Arrange
        Base base1 = new Base();
        Base base2 = new Base();

        // Act
        base1.setId(1);
        base2.setId(2);

        base1.setAtivo(true);
        base2.setAtivo(false);

        // Assert
        assertEquals(Integer.valueOf(1), base1.getId());
        assertEquals(Integer.valueOf(2), base2.getId());
        assertTrue(base1.getAtivo());
        assertFalse(base2.getAtivo());
    }

    @Test
    @DisplayName("Deve retornar null para todos os atributos inicialmente")
    void testRetornarNullParaTodosAtributosInicialmente() {
        // Arrange & Act
        Base novaBase = new Base();

        // Assert
        assertNull(novaBase.getId());
        assertNull(novaBase.getDataCriacao());
        assertNull(novaBase.getDataAlteracao());
        assertNull(novaBase.getAtivo());
    }

    @Test
    @DisplayName("Deve manter os valores após múltiplas alterações")
    void testMantendoValoresAposMultiplasAlteracoes() {
        // Arrange
        Integer id = 10;
        LocalDateTime data = LocalDateTime.now();
        Boolean ativo = true;

        // Act
        base.setId(id);
        base.setDataCriacao(data);
        base.setAtivo(ativo);

        // Assert - primeiro ciclo
        assertEquals(id, base.getId());
        assertEquals(data, base.getDataCriacao());
        assertEquals(ativo, base.getAtivo());

        // Act - segundo ciclo (sem alteração)
        // Assert
        assertEquals(id, base.getId());
        assertEquals(data, base.getDataCriacao());
        assertEquals(ativo, base.getAtivo());
    }

    @Test
    @DisplayName("Deve validar que ID pode ser modificado após null")
    void testModificarIdAposNull() {
        // Arrange
        base.setId(1);
        base.setId(null);

        // Act
        base.setId(2);

        // Assert
        assertEquals(Integer.valueOf(2), base.getId());
    }

    @Test
    @DisplayName("Deve validar que datas podem ser diferentes")
    void testDatasPodemSerDiferentes() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        LocalDateTime dataAlteracao = LocalDateTime.of(2023, 12, 31, 23, 59, 59);

        // Act
        base.setDataCriacao(dataCriacao);
        base.setDataAlteracao(dataAlteracao);

        // Assert
        assertNotEquals(dataCriacao, dataAlteracao);
        assertEquals(dataCriacao, base.getDataCriacao());
        assertEquals(dataAlteracao, base.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve validar que datas podem ser iguais")
    void testDatasPodemSerIguais() {
        // Arrange
        LocalDateTime data = LocalDateTime.of(2023, 6, 15, 12, 30, 45);

        // Act
        base.setDataCriacao(data);
        base.setDataAlteracao(data);

        // Assert
        assertEquals(data, base.getDataCriacao());
        assertEquals(data, base.getDataAlteracao());
        assertEquals(base.getDataCriacao(), base.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve suportar valores extremos de ID")
    void testSuportarValoresExtremosDeId() {
        // Act & Assert
        base.setId(Integer.MIN_VALUE);
        assertEquals(Integer.valueOf(Integer.MIN_VALUE), base.getId());

        base.setId(Integer.MAX_VALUE);
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), base.getId());
    }
}
