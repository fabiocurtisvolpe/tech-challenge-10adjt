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

import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

/**
 * Testes unitários para AtualizarSenhaUsuarioUseCase
 * 
 * Testa os cenários de atualização de senha com sucesso e falhas
 * 
 * @author Fabio
 * @since 2025-12-03
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarSenhaUsuarioUseCase - Testes Unitários")
class AtualizarSenhaUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    private AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase;

    @BeforeEach
    void setUp() {
        atualizarSenhaUsuarioUseCase = AtualizarSenhaUsuarioUseCase.create(usuarioRepositoryPort);
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso quando usuário existe")
    void testAtualizarSenhaComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuarioExistente = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senhaAntiga123",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        TrocarSenhaUsuarioDTO dto = new TrocarSenhaUsuarioDTO("joao@email.com", "senhaNova456", "senhaNova456Codificada");

        Usuario usuarioAtualizado = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senhaNova456Codificada",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryPort.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarSenhaUsuarioUseCase.run(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("senhaNova456Codificada", resultado.getSenha());
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("joao@email.com");
        verify(usuarioRepositoryPort, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void testAtualizarSenhaUsuarioNaoExistente() {
        // Arrange
        TrocarSenhaUsuarioDTO dto = new TrocarSenhaUsuarioDTO("naoexiste@email.com", "naoexiste@email.com", "naoexiste@email.com");

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> atualizarSenhaUsuarioUseCase.run(dto));
        verify(usuarioRepositoryPort, times(1)).obterPorEmail("naoexiste@email.com");
        verify(usuarioRepositoryPort, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve preservar dados do usuário ao trocar senha")
    void testAtualizarSenhaPreservandoDados() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuarioExistente = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senhaAntiga123",
                TipoUsuarioEnum.DONO_RESTAURANTE,
                enderecos,
                true);

        TrocarSenhaUsuarioDTO dto = new TrocarSenhaUsuarioDTO("joao@email.com", "senhaNova456", "senhaNova456Codificada");

        Usuario usuarioAtualizado = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senhaNova456Codificada",
                TipoUsuarioEnum.DONO_RESTAURANTE,
                enderecos,
                true);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryPort.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarSenhaUsuarioUseCase.run(dto);

        // Assert
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, resultado.getTipoUsuario());
        assertEquals(1, resultado.getId());
    }

    @Test
    @DisplayName("Deve permitir trocar de uma senha longa para outra")
    void testAtualizarSenhaComSenhasLongas() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", "Apto 10", "Centro", "Perto da padaria",
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuarioExistente = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senhaAntiga123456789Muito",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        TrocarSenhaUsuarioDTO dto = new TrocarSenhaUsuarioDTO("joao@email.com", "senhaNova", "senhaNovaMuitoLonga456789123456");

        Usuario usuarioAtualizado = new Usuario(
                1,
                "João Silva",
                "joao@email.com",
                "senhaNovaMuitoLonga456789123456",
                TipoUsuarioEnum.CLIENTE,
                enderecos,
                true);

        when(usuarioRepositoryPort.obterPorEmail(dto.email())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryPort.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarSenhaUsuarioUseCase.run(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("senhaNovaMuitoLonga456789123456", resultado.getSenha());
    }
}
