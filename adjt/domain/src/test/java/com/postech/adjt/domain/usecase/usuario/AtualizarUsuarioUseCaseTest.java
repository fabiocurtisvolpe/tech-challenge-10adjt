package com.postech.adjt.domain.usecase.usuario;

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

import com.postech.adjt.domain.dto.AtualizaUsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

/**
 * Testes unitários para AtualizarUsuarioUseCase
 * 
 * Testa os cenários de atualização de usuário com sucesso e falhas
 * 
 * @author Fabio
 * @since 2025-12-03
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarUsuarioUseCase - Testes Unitários")
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @BeforeEach
    void setUp() {
        atualizarUsuarioUseCase = AtualizarUsuarioUseCase.create(usuarioRepositoryPort);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso quando dados são válidos")
    void testAtualizarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua B", "456", "Apto 20", "Bairro", "Perto da escola",
                "87654-321", "Rio de Janeiro", "RJ", true, null));

        Usuario usuarioExistente = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        AtualizaUsuarioDTO dto = new AtualizaUsuarioDTO(
                "João Silva Atualizado",
                "joao@email.com",
                enderecos);

        Usuario usuarioAtualizado = new Usuario(
                1,
                "João Silva Atualizado",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryPort.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.run(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva Atualizado", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("joao@email.com");
        verify(usuarioRepositoryPort, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void testAtualizarUsuarioNaoExistente() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua B", "456", "Apto 20", "Bairro", "Perto da escola",
                "87654-321", "Rio de Janeiro", "RJ", true, null));

        AtualizaUsuarioDTO dto = new AtualizaUsuarioDTO(
                "João Silva",
                "naoexiste@email.com",
                enderecos);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> atualizarUsuarioUseCase.run(dto));
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("naoexiste@email.com");
        verify(usuarioRepositoryPort, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve manter email original durante atualização")
    void testAtualizarUsuarioMantendoEmail() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua B", "456", "Apto 20", "Bairro", "Perto da escola",
                "87654-321", "Rio de Janeiro", "RJ", true, null));

        Usuario usuarioExistente = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        AtualizaUsuarioDTO dto = new AtualizaUsuarioDTO(
                "João Silva Updated",
                "joao@email.com",
                enderecos);

        Usuario usuarioAtualizado = new Usuario(
                1,
                "João Silva Updated",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryPort.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.run(dto);

        // Assert
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve manter tipo de usuário original durante atualização")
    void testAtualizarUsuarioMantendoTipo() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua B", "456", "Apto 20", "Bairro", "Perto da escola",
                "87654-321", "Rio de Janeiro", "RJ", true, null));

        Usuario usuarioExistente = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.DONO_RESTAURANTE,
                enderecos,
                true);

        AtualizaUsuarioDTO dto = new AtualizaUsuarioDTO(
                "João Silva Updated",
                "joao@email.com",
                enderecos);

        Usuario usuarioAtualizado = new Usuario(
                1,
                "João Silva Updated",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.DONO_RESTAURANTE,
                enderecos,
                true);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryPort.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.run(dto);

        // Assert
        assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, resultado.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve manter senha original durante atualização")
    void testAtualizarUsuarioMantendoSenha() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua B", "456", "Apto 20", "Bairro", "Perto da escola",
                "87654-321", "Rio de Janeiro", "RJ", true, null));

        Usuario usuarioExistente = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senhaOriginal123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        AtualizaUsuarioDTO dto = new AtualizaUsuarioDTO(
                "João Silva Updated",
                "joao@email.com",
                enderecos);

        Usuario usuarioAtualizado = new Usuario(
                1,
                "João Silva Updated",
                "joao@email.com",
                "senhaOriginal123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryPort.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.run(dto);

        // Assert
        assertEquals("senhaOriginal123", resultado.getSenha());
    }
}
