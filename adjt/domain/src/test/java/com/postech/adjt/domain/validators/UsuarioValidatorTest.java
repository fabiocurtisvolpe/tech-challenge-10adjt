package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioValidator - Testes Unitários")
class UsuarioValidatorTest {

    private List<Endereco> criarEnderecos() {
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = Endereco.builder()
            .logradouro("Rua Teste")
            .numero("123")
            .complemento("Apto 101")
            .bairro("Bairro Teste")
            .pontoReferencia("Perto de algo")
            .cep("12345-678")
            .municipio("São Paulo")
            .uf("SP")
            .principal(true)
            .build();
        enderecos.add(endereco);
        return enderecos;
    }

    private TipoUsuario criarTipoUsuarioValido() {
        return TipoUsuarioGenrico.builder()
            .id(1)
            .nome("CLIENTE")
            .descricao("Cliente do sistema")
            .build();
    }

    private Usuario criarUsuarioValido() {
        return Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();
    }

    // ===== TESTES: validarParaCriacao =====

    @Test
    @DisplayName("Deve validar usuario válido para criação com sucesso")
    void testValidarParaCriacaoComSucesso() {
        // Arrange
        Usuario usuario = criarUsuarioValido();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario nulo para criação")
    void testValidarParaCriacaoComUsuarioNulo() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com nome nulo para criação")
    void testValidarParaCriacaoComNomeNulo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome(null)
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com nome vazio para criação")
    void testValidarParaCriacaoComNomeVazio() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("  ")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com nome menor que 3 caracteres")
    void testValidarParaCriacaoComNomeMuitoCurto() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Jo")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

     @Test
    @DisplayName("Deve lançar exceção ao validar usuario com nome maior que 50 caracteres")
    void testValidarParaCriacaoComNomeMuitoLongo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Jooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooão")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve validar usuario com nome exatamente 3 caracteres")
    void testValidarParaCriacaoComNomeExatamente3Caracteres() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com email nulo para criação")
    void testValidarParaCriacaoComEmailNulo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email(null)
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com email vazio para criação")
    void testValidarParaCriacaoComEmailVazio() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("  ")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com email inválido para criação")
    void testValidarParaCriacaoComEmailInvalido() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@invalido")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com senha nula para criação")
    void testValidarParaCriacaoComSenhaNula() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha(null)
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com senha vazia para criação")
    void testValidarParaCriacaoComSenhaVazia() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com senha menor que 6 caracteres")
    void testValidarParaCriacaoComSenhaMuitoCurta() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senha")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve validar usuario com senha exatamente 6 caracteres")
    void testValidarParaCriacaoComSenhaExatamente6Caracteres() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senha6")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com tipo nulo para criação")
    void testValidarParaCriacaoComTipoNulo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(null)
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaCriacao(usuario);
        });
    }

    // ===== TESTES: validarParaAtualizacao =====

    @Test
    @DisplayName("Deve validar usuario válido para atualização com sucesso")
    void testValidarParaAtualizacaoComSucesso() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(5)
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaAtualizacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario nulo para atualização")
    void testValidarParaAtualizacaoComUsuarioNulo() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com ID nulo para atualização")
    void testValidarParaAtualizacaoComIdNulo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com ID zero para atualização")
    void testValidarParaAtualizacaoComIdZero() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(0)
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com ID negativo para atualização")
    void testValidarParaAtualizacaoComIdNegativo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(-5)
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve validar usuario com ID válido para atualização")
    void testValidarParaAtualizacaoComIdValido() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(1)
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaAtualizacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com nome nulo para atualização")
    void testValidarParaAtualizacaoComNomeNulo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome(null)
            .email("maria@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(5)
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com nome vazio para atualização")
    void testValidarParaAtualizacaoComNomeVazio() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("  ")
            .email("maria@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(5)
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com nome menor que 3 caracteres para atualização")
    void testValidarParaAtualizacaoComNomeMuitoCurto() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Ma")
            .email("maria@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(5)
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve validar usuario com nome válido para atualização")
    void testValidarParaAtualizacaoComNomeValido() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Maria Silva Oliveira")
            .email("maria@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(5)
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaAtualizacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com email nulo para atualização")
    void testValidarParaAtualizacaoComEmailNulo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Maria Silva")
            .email(null)
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(5)
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com email vazio para atualização")
    void testValidarParaAtualizacaoComEmailVazio() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Maria Silva")
            .email("  ")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(5)
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar usuario com email inválido para atualização")
    void testValidarParaAtualizacaoComEmailInvalido() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Maria Silva")
            .email("maria@invalido")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(5)
            .build();

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            UsuarioValidator.validarParaAtualizacao(usuario);
        });
    }

    @Test
    @DisplayName("Deve validar usuario com todos os parâmetros válidos para atualização")
    void testValidarParaAtualizacaoComTodosParametrosValidos() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Pedro Oliveira Santos")
            .email("pedro@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(10)
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaAtualizacao(usuario));
    }

    @Test
    @DisplayName("Deve validar email com domínio complexo")
    void testValidarEmailComDominioComplexo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Ana Silva")
            .email("ana.silva@empresa.com.br")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve validar usuario com nome contendo espaços")
    void testValidarNomeComEspacos() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("   João Silva   ")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve validar usuario com ID máximo para atualização")
    void testValidarParaAtualizacaoComIdMaximo() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(criarTipoUsuarioValido())
            .enderecos(criarEnderecos())
            .id(Integer.MAX_VALUE)
            .build();

        // Act & Assert
        assertDoesNotThrow(() -> UsuarioValidator.validarParaAtualizacao(usuario));
    }
}
