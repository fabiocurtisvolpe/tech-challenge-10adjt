package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;
import com.postech.adjt.domain.exception.NotificacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoUsuarioValidator - Testes Unitários")
class TipoUsuarioValidatorTest {

    @Test
    @DisplayName("Deve validar TipoUsuario válido com sucesso")
    void testValidarTipoUsuarioValido() {
        // Arrange
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome("CLIENTE")
                .descricao("Tipo de usuário cliente padrão")
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoUsuarioValidator.validar(tipoUsuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoUsuario nulo")
    void testValidarTipoUsuarioNulo() {
        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoUsuarioValidator.validar(null);
        });
        assertEquals(MensagemUtil.TIPO_USUARIO_NULO_VALIDACAO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoUsuario com nome nulo")
    void testValidarTipoUsuarioComNomeNulo() {
        // Arrange
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome(null)
                .descricao("Descrição")
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoUsuarioValidator.validar(tipoUsuario);
        });
        assertEquals(MensagemUtil.NOME_EM_BRANCO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoUsuario com nome vazio")
    void testValidarTipoUsuarioComNomeVazio() {
        // Arrange
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome("  ")
                .descricao("Descrição")
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoUsuarioValidator.validar(tipoUsuario);
        });
        assertEquals(MensagemUtil.NOME_EM_BRANCO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoUsuario com nome menor que 3 caracteres")
    void testValidarTipoUsuarioComNomeMuitoCurto() {
        // Arrange
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome("AB")
                .descricao("Descrição")
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoUsuarioValidator.validar(tipoUsuario);
        });
        assertEquals(MensagemUtil.NOME_MINIMO_CARACTERES, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoUsuario com nome maior que 50 caracteres")
    void testValidarTipoUsuarioComNomeMuitoLongo() {
        // Arrange
        String nomeLongo = "A".repeat(51);
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome(nomeLongo)
                .descricao("Descrição")
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoUsuarioValidator.validar(tipoUsuario);
        });
        assertEquals(MensagemUtil.NOME_MAXIMO_CARACTERES, exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar TipoUsuario com nome exatamente 3 caracteres")
    void testValidarTipoUsuarioComNomeExatamente3Caracteres() {
        // Arrange
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome("ABC")
                .descricao("Descrição")
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoUsuarioValidator.validar(tipoUsuario));
    }

    @Test
    @DisplayName("Deve validar TipoUsuario com nome exatamente 50 caracteres")
    void testValidarTipoUsuarioComNomeExatamente50Caracteres() {
        // Arrange
        String nome50 = "A".repeat(50);
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome(nome50)
                .descricao("Descrição")
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoUsuarioValidator.validar(tipoUsuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar TipoUsuario com descrição maior que 1000 caracteres")
    void testValidarTipoUsuarioComDescricaoMuitoLonga() {
        // Arrange
        String descricaoLonga = "A".repeat(1001);
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome("CLIENTE")
                .descricao(descricaoLonga)
                .build();

        // Act & Assert
        NotificacaoException exception = assertThrows(NotificacaoException.class, () -> {
            TipoUsuarioValidator.validar(tipoUsuario);
        });
        assertEquals(MensagemUtil.DESCRICAO_MAXIMO_CARACTERES, exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar TipoUsuario com descrição exatamente 1000 caracteres")
    void testValidarTipoUsuarioComDescricaoExatamente200Caracteres() {
        // Arrange
        String descricao1000 = "A".repeat(1000);
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome("CLIENTE")
                .descricao(descricao1000)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoUsuarioValidator.validar(tipoUsuario));
    }

    @Test
    @DisplayName("Deve validar TipoUsuario sem descrição (null)")
    void testValidarTipoUsuarioSemDescricao() {
        // Arrange
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome("CLIENTE")
                .descricao(null)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoUsuarioValidator.validar(tipoUsuario));
    }

    @Test
    @DisplayName("Deve validar TipoUsuario com descrição vazia")
    void testValidarTipoUsuarioComDescricaoVazia() {
        // Arrange
        TipoUsuario tipoUsuario = TipoUsuarioGenrico.builder()
                .id(1)
                .nome("CLIENTE")
                .descricao("")
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> TipoUsuarioValidator.validar(tipoUsuario));
    }
}
