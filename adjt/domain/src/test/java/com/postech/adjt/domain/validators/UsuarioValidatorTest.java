package com.postech.adjt.domain.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.validators.UsuarioValidator; // Necessário para acesso aos métodos estáticos

/**
 * Testes unitários para UsuarioValidator
 * 
 * Testa validações de regras de negócio para usuários
 * 
 * @author Fabio
 * @since 2025-12-03
 */
@DisplayName("UsuarioValidator - Testes Unitários")
@SuppressWarnings("unused")
class UsuarioValidatorTest {

    @Test
    @DisplayName("Deve validar usuário com sucesso para criação quando dados são válidos")
    void testValidarParaCriacaoComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert - Não deve lançar exceção
        assertDoesNotThrow(() -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é inválido")
    void testValidarParaCriacaoComEmailInvalido() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                "João Silva",
                "emailinvalido",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha tem menos de 6 caracteres")
    void testValidarParaCriacaoComSenhaInvalida() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                "João Silva",
                "joao@email.com",
                "123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
    void testValidarParaCriacaoComNomeInvalido() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                "Jo",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário é inválido")
    void testValidarParaCriacaoComTipoUsuarioInvalido() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                null,
                enderecos);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve validar usuário para atualização com sucesso quando dados são válidos")
    void testValidarParaAtualizacaoComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        // Act & Assert - Não deve lançar exceção
        assertDoesNotThrow(() -> UsuarioValidator.validarParaAtualizacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é nulo na atualização")
    void testValidarParaAtualizacaoComIdNulo() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                null,
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaAtualizacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é menor ou igual a zero na atualização")
    void testValidarParaAtualizacaoComIdInvalido() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                0,
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaAtualizacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é nulo")
    void testValidarParaCriacaoComEmailNulo() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                "João Silva",
                null,
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é nula")
    void testValidarParaCriacaoComSenhaNula() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                "João Silva",
                "joao@email.com",
                null,
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaCriacao(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void testValidarParaCriacaoComNomeNulo() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                null,
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> UsuarioValidator.validarParaCriacao(usuario));
    }
}
