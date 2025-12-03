package com.postech.adjt.domain.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.dto.NovoUsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;
import com.postech.adjt.domain.usecase.usuario.CadastrarUsuarioUseCase;

/**
 * Testes unitários para CadastrarUsuarioUseCase
 * 
 * Testa os cenários de cadastro de usuário com sucesso e falhas
 * 
 * @author Fabio
 * @since 2025-12-03
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarUsuarioUseCase - Testes Unitários")
class CadastrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @BeforeEach
    void setUp() {
        cadastrarUsuarioUseCase = CadastrarUsuarioUseCase.create(usuarioRepositoryPort);
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso quando dados são válidos")
    void testCadastrarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        NovoUsuarioDTO dto = new NovoUsuarioDTO(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        Usuario usuarioEsperado = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.empty());
        when(usuarioRepositoryPort.criar(any(Usuario.class))).thenReturn(usuarioEsperado);

        // Act
        Usuario resultado = cadastrarUsuarioUseCase.run(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("joao@email.com");
        verify(usuarioRepositoryPort, times(1)).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário já existe")
    void testCadastrarUsuarioJaExistente() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        NovoUsuarioDTO dto = new NovoUsuarioDTO(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        Usuario usuarioExistente = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.of(usuarioExistente));

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> cadastrarUsuarioUseCase.run(dto));
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("joao@email.com");
        verify(usuarioRepositoryPort, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é inválido")
    void testCadastrarUsuarioComEmailInvalido() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        NovoUsuarioDTO dto = new NovoUsuarioDTO(
                "João Silva",
                "emailinvalido",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cadastrarUsuarioUseCase.run(dto));
        verify(usuarioRepositoryPort, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é menor que 6 caracteres")
    void testCadastrarUsuarioComSenhaInvalida() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        NovoUsuarioDTO dto = new NovoUsuarioDTO(
                "João Silva",
                "joao@email.com",
                "123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cadastrarUsuarioUseCase.run(dto));
        verify(usuarioRepositoryPort, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é menor que 3 caracteres")
    void testCadastrarUsuarioComNomeInvalido() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        NovoUsuarioDTO dto = new NovoUsuarioDTO(
                "Jo",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cadastrarUsuarioUseCase.run(dto));
        verify(usuarioRepositoryPort, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário é nulo")
    void testCadastrarUsuarioComTipoNulo() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        NovoUsuarioDTO dto = new NovoUsuarioDTO(
                "João Silva",
                "joao@email.com",
                "senha123",
                null,
                enderecos);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cadastrarUsuarioUseCase.run(dto));
        verify(usuarioRepositoryPort, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de endereços é vazia")
    void testCadastrarUsuarioSemEnderecos() {
        // Arrange
        NovoUsuarioDTO dto = new NovoUsuarioDTO(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                new ArrayList<>());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cadastrarUsuarioUseCase.run(dto));
        verify(usuarioRepositoryPort, never()).criar(any(Usuario.class));
    }
}
