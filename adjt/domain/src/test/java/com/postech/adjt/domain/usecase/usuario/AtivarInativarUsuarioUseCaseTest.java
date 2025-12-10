package com.postech.adjt.domain.usecase.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Testes unitários para AtivarInativarUsuarioUseCase
 * 
 * Testa a ativação e desativação de usuários no sistema
 * 
 * @author Fabio
 * @since 2025-12-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AtivarInativarUsuarioUseCase - Testes Unitários")
class AtivarInativarUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    private AtivarInativarUsuarioUseCase useCase;

    private Usuario usuarioExistente;
    private TipoUsuario tipoUsuarioValido;
    private List<Endereco> enderecos;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarUsuarioUseCase.create(usuarioRepository);

        // Preparar dados de teste
        enderecos = new ArrayList<>();
        enderecos.add(Endereco.builder()
            .logradouro("Rua Teste")
            .numero("123")
            .complemento("Apto 101")
            .bairro("Centro")
            .pontoReferencia("Perto da praça")
            .cep("12345-678")
            .municipio("São Paulo")
            .uf("SP")
            .principal(true)
            .build());

        tipoUsuarioValido = TipoUsuario.builder()
            .id(1)
            .nome("CLIENTE")
            .descricao("CLIENTE")
            .build();

        usuarioExistente = Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(tipoUsuarioValido)
            .enderecos(enderecos)
            .ativo(true)
            .build();
    }

    @Test
    @DisplayName("Deve ativar usuário com sucesso")
    void testAtivarUsuarioComSucesso() {
        // Arrange
        Usuario usuarioAtivado = Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(tipoUsuarioValido)
            .enderecos(enderecos)
            .ativo(true)
            .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
            .thenReturn(usuarioAtivado);

        // Act
        Usuario resultado = useCase.run("joao@email.com", true);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getAtivo());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).obterPorEmail("joao@email.com");
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve desativar usuário com sucesso")
    void testDesativarUsuarioComSucesso() {
        // Arrange
        Usuario usuarioDesativado = Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(tipoUsuarioValido)
            .enderecos(enderecos)
            .ativo(false)
            .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
            .thenReturn(usuarioDesativado);

        // Act
        Usuario resultado = useCase.run("joao@email.com", false);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.getAtivo());
        assertEquals("João Silva", resultado.getNome());
        verify(usuarioRepository, times(1)).obterPorEmail("joao@email.com");
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado para ativar")
    void testAtivarUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.obterPorEmail("inexistente@email.com"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run("inexistente@email.com", true);
        });

        verify(usuarioRepository, times(1)).obterPorEmail("inexistente@email.com");
        verify(usuarioRepository, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado para desativar")
    void testDesativarUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.obterPorEmail("inexistente@email.com"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run("inexistente@email.com", false);
        });

        verify(usuarioRepository, times(1)).obterPorEmail("inexistente@email.com");
        verify(usuarioRepository, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve manter dados do usuário ao ativar")
    void testMantendoDadosAoAtivar() {
        // Arrange
        Usuario usuarioAtualizado = Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(tipoUsuarioValido)
            .enderecos(enderecos)
            .ativo(true)
            .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
            .thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = useCase.run("joao@email.com", true);

        // Assert
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertEquals(tipoUsuarioValido, resultado.getTipoUsuario());
        assertEquals(enderecos.size(), resultado.getEnderecos().size());
    }

    @Test
    @DisplayName("Deve ativar múltiplos usuários sequencialmente")
    void testAtivarMultiplosUsuarios() {
        // Arrange
        Usuario usuario1 = Usuario.builder()
            .id(1)
            .nome("João")
            .email("joao@email.com")
            .senha("senha1")
            .tipoUsuario(tipoUsuarioValido)
            .enderecos(new ArrayList<>())
            .ativo(true)
            .build();

        Usuario usuario2 = Usuario.builder()
            .id(2)
            .nome("Maria")
            .email("maria@email.com")
            .senha("senha2")
            .tipoUsuario(tipoUsuarioValido)
            .enderecos(new ArrayList<>())
            .ativo(true)
            .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.obterPorEmail("maria@email.com"))
            .thenReturn(Optional.of(usuario2));
        when(usuarioRepository.atualizar(any(Usuario.class)))
            .thenReturn(usuario1)
            .thenReturn(usuario2);

        // Act
        Usuario resultado1 = useCase.run("joao@email.com", true);
        Usuario resultado2 = useCase.run("maria@email.com", true);

        // Assert
        assertNotNull(resultado1);
        assertNotNull(resultado2);
        assertTrue(resultado1.getAtivo());
        assertTrue(resultado2.getAtivo());
        verify(usuarioRepository, times(2)).obterPorEmail(anyString());
        verify(usuarioRepository, times(2)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve desativar usuário mantendo seus endereços")
    void testDesativarUsuarioMantendoEnderecos() {
        // Arrange
        Usuario usuarioDesativado = Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(tipoUsuarioValido)
            .enderecos(enderecos)
            .ativo(false)
            .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
            .thenReturn(usuarioDesativado);

        // Act
        Usuario resultado = useCase.run("joao@email.com", false);

        // Assert
        assertFalse(resultado.getAtivo());
        assertNotNull(resultado.getEnderecos());
        assertEquals(1, resultado.getEnderecos().size());
        assertEquals("Rua Teste", resultado.getEnderecos().get(0).getLogradouro());
    }

    @Test
    @DisplayName("Deve chamar o repositório corretamente ao ativar")
    void testChamadasRepositorioAoAtivar() {
        // Arrange
        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
            .thenReturn(usuarioExistente);

        // Act
        useCase.run("joao@email.com", true);

        // Assert
        verify(usuarioRepository).obterPorEmail("joao@email.com");
        verify(usuarioRepository).atualizar(any(Usuario.class));
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve usar factory para criar usuario atualizado")
    void testUsandoFactoryParaAtualizacao() {
        // Arrange
        Usuario usuarioAtualizado = Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senhaSegura123")
            .tipoUsuario(tipoUsuarioValido)
            .enderecos(enderecos)
            .ativo(true)
            .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
            .thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = useCase.run("joao@email.com", true);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getAtivo());
        assertEquals(1, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }

}
