package com.postech.adjt.domain.usecase.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

/**
 * Testes unitários para ObterUsuarioPorEmailUseCase
 * 
 * Testa os cenários de busca de usuário por email
 * 
 * @author Fabio
 * @since 2025-12-03
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ObterUsuarioPorEmailUseCase - Testes Unitários")
class ObterUsuarioPorEmailUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    private ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase;

    @BeforeEach
    void setUp() {
        obterUsuarioPorEmailUseCase = ObterUsuarioPorEmailUseCase.create(usuarioRepositoryPort);
    }

    @Test
    @DisplayName("Deve obter usuário com sucesso quando email existe")
    void testObterUsuarioPorEmailComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuarioEsperado = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(usuarioRepositoryPort.obterPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioEsperado));

        // Act
        Optional<Usuario> resultado = obterUsuarioPorEmailUseCase.run("joao@email.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        assertEquals("joao@email.com", resultado.get().getEmail());
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é nulo")
    void testObterUsuarioPorEmailNulo() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> obterUsuarioPorEmailUseCase.run(null));
        verify(usuarioRepositoryPort, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é vazio")
    void testObterUsuarioPorEmailVazio() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> obterUsuarioPorEmailUseCase.run(""));
        verify(usuarioRepositoryPort, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email contém apenas espaços")
    void testObterUsuarioPorEmailApenasEspacos() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> obterUsuarioPorEmailUseCase.run("   "));
        verify(usuarioRepositoryPort, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void testObterUsuarioPorEmailNaoExistente() {
        // Arrange
        when(usuarioRepositoryPort.obterPorEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> obterUsuarioPorEmailUseCase.run("naoexiste@email.com"));
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("naoexiste@email.com");
    }

    @Test
    @DisplayName("Deve retornar Optional com usuário quando encontrado")
    void testObterUsuarioPorEmailRetornaOptional() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Usuario usuario = new Usuario(
                "Maria Silva",
                "maria@email.com",
                "senha456",
                TipoUsuarioEnum.DONO_RESTAURANTE,
                enderecos);

        when(usuarioRepositoryPort.obterPorEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = obterUsuarioPorEmailUseCase.run("maria@email.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Maria Silva", resultado.get().getNome());
        assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, resultado.get().getTipoUsuario());
    }
}
