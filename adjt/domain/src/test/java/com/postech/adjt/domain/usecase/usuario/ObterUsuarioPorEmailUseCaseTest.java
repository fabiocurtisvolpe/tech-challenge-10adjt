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

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

/**
 * Testes unitários para ObterUsuarioPorEmailUseCase
 * 
 * Testa a recuperação de usuários pelo email
 * 
 * @author Fabio
 * @since 2025-12-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ObterUsuarioPorEmailUseCase - Testes Unitários")
class ObterUsuarioPorEmailUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private ObterUsuarioPorEmailUseCase useCase;

    private Usuario usuario;
    private List<Endereco> enderecos;

    @BeforeEach
    void setUp() {
        useCase = ObterUsuarioPorEmailUseCase.create(usuarioRepository);

        // Preparar endereços
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

        usuario = Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.CLIENTE)
            .enderecos(enderecos)
            .ativo(true)
            .build();
    }

    @Test
    @DisplayName("Deve obter usuário por email com sucesso")
    void testObterUsuarioPorEmailComSucesso() {
        // Arrange
        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run("joao@email.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        assertEquals("joao@email.com", resultado.get().getEmail());
        verify(usuarioRepository, times(1)).obterPorEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é nulo")
    void testObterUsuarioPorEmailNulo() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(null);
        });

        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email está vazio")
    void testObterUsuarioPorEmailVazio() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run("");
        });

        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email contém apenas espaços")
    void testObterUsuarioPorEmailApenasEspacos() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run("   ");
        });

        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void testObterUsuarioPorEmailNaoEncontrado() {
        // Arrange
        when(usuarioRepository.obterPorEmail("inexistente@email.com"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run("inexistente@email.com");
        });

        verify(usuarioRepository, times(1)).obterPorEmail("inexistente@email.com");
    }

    @Test
    @DisplayName("Deve retornar usuário com dados completos")
    void testRetornarUsuarioComDadosCompletos() {
        // Arrange
        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run("joao@email.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("João Silva", resultado.get().getNome());
        assertEquals("joao@email.com", resultado.get().getEmail());
        assertEquals("senha123", resultado.get().getSenha());
        assertEquals(TipoUsuarioEnum.CLIENTE, resultado.get().getTipoUsuario());
        assertTrue(resultado.get().getAtivo());
    }

    @Test
    @DisplayName("Deve retornar usuário com endereços")
    void testRetornarUsuarioComEnderecos() {
        // Arrange
        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run("joao@email.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertNotNull(resultado.get().getEnderecos());
        assertEquals(1, resultado.get().getEnderecos().size());
        assertEquals("Rua Teste", resultado.get().getEnderecos().get(0).getLogradouro());
    }

    @Test
    @DisplayName("Deve obter usuários de diferentes emails")
    void testObterUsuariosDiferentesEmails() {
        // Arrange
        Usuario usuario2 = Usuario.builder()
            .id(2)
            .nome("Maria Silva")
            .email("maria@email.com")
            .senha("senha456")
            .tipoUsuario(TipoUsuarioEnum.FORNECEDOR)
            .enderecos(enderecos)
            .ativo(true)
            .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuario));
        when(usuarioRepository.obterPorEmail("maria@email.com"))
            .thenReturn(Optional.of(usuario2));

        // Act
        Optional<Usuario> resultado1 = useCase.run("joao@email.com");
        Optional<Usuario> resultado2 = useCase.run("maria@email.com");

        // Assert
        assertTrue(resultado1.isPresent());
        assertTrue(resultado2.isPresent());
        assertEquals("João Silva", resultado1.get().getNome());
        assertEquals("Maria Silva", resultado2.get().getNome());
    }

    @Test
    @DisplayName("Deve chamar repositório com email correto")
    void testChamarRepositorioComEmailCorreto() {
        // Arrange
        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuario));

        // Act
        useCase.run("joao@email.com");

        // Assert
        verify(usuarioRepository).obterPorEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve validar email antes de consultar repositório")
    void testValidarEmailAntesDeChamarRepositorio() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run("");
        });

        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve obter usuário inativo por email")
    void testObterUsuarioInativoPorEmail() {
        // Arrange
        Usuario usuarioInativo = Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.CLIENTE)
            .enderecos(enderecos)
            .ativo(false)
            .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuarioInativo));

        // Act
        Optional<Usuario> resultado = useCase.run("joao@email.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertFalse(resultado.get().getAtivo());
    }

    @Test
    @DisplayName("Deve retornar Optional com usuário válido")
    void testRetornarOptionalComUsuarioValido() {
        // Arrange
        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run("joao@email.com");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve ser case-sensitive para email")
    void testEmailCaseSensitive() {
        // Arrange
        when(usuarioRepository.obterPorEmail("joao@email.com"))
            .thenReturn(Optional.of(usuario));
        when(usuarioRepository.obterPorEmail("JOAO@EMAIL.COM"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertTrue(useCase.run("joao@email.com").isPresent());
        assertThrows(NotificacaoException.class, () -> {
            useCase.run("JOAO@EMAIL.COM");
        });
    }

}
