package com.postech.adjt.domain.usecase.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Testes unitários para ObterUsuarioPorIdUseCase
 * 
 * Testa a recuperação de usuários pelo ID
 * 
 * @author Fabio
 * @since 2025-12-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ObterUsuarioPorIdUseCase - Testes Unitários")
class ObterUsuarioPorIdUseCaseTest {

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    private ObterUsuarioPorIdUseCase useCase;

    private Usuario usuario;
    private TipoUsuario tipoUsuarioValido;
    private List<Endereco> enderecos;

    @BeforeEach
    void setUp() {
        useCase = ObterUsuarioPorIdUseCase.create(usuarioRepository);

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

        tipoUsuarioValido = TipoUsuarioFactory.atualizar(1, "CLIENTE", "CLIENTE", true, false);

        usuario = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senha123")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();
    }

    @Test
    @DisplayName("Deve obter usuário por ID com sucesso")
    void testObterUsuarioPorIdComSucesso() {
        // Arrange
        when(usuarioRepository.obterPorId(1))
                .thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("João Silva", resultado.get().getNome());
        assertEquals("joao@email.com", resultado.get().getEmail());
        verify(usuarioRepository, times(1)).obterPorId(1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é nulo")
    void testObterUsuarioPorIdNulo() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(null);
        });

        verify(usuarioRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é negativo")
    void testObterUsuarioPorIdNegativo() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1);
        });

        verify(usuarioRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é zero")
    void testObterUsuarioPorIdZero() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0);
        });

        verify(usuarioRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void testObterUsuarioPorIdNaoEncontrado() {
        // Arrange
        when(usuarioRepository.obterPorId(999))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999);
        });

        verify(usuarioRepository, times(1)).obterPorId(999);
    }

    @Test
    @DisplayName("Deve retornar usuário com dados completos")
    void testRetornarUsuarioComDadosCompletos() {
        // Arrange
        when(usuarioRepository.obterPorId(1))
                .thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("João Silva", resultado.get().getNome());
        assertEquals("joao@email.com", resultado.get().getEmail());
        assertEquals("senha123", resultado.get().getSenha());
        assertEquals(tipoUsuarioValido, resultado.get().getTipoUsuario());
        assertTrue(resultado.get().getAtivo());
    }

    @Test
    @DisplayName("Deve retornar usuário com endereços")
    void testRetornarUsuarioComEnderecos() {
        // Arrange
        when(usuarioRepository.obterPorId(1))
                .thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertNotNull(resultado.get().getEnderecos());
        assertEquals(1, resultado.get().getEnderecos().size());
        assertEquals("Rua Teste", resultado.get().getEnderecos().get(0).getLogradouro());
    }

    @Test
    @DisplayName("Deve obter usuários de diferentes IDs")
    void testObterUsuariosDiferentesIds() {
        // Arrange
        Usuario usuario2 = Usuario.builder()
                .id(2)
                .nome("Maria Silva")
                .email("maria@email.com")
                .senha("senha456")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorId(1))
                .thenReturn(Optional.of(usuario));
        when(usuarioRepository.obterPorId(2))
                .thenReturn(Optional.of(usuario2));

        // Act
        Optional<Usuario> resultado1 = useCase.run(1);
        Optional<Usuario> resultado2 = useCase.run(2);

        // Assert
        assertTrue(resultado1.isPresent());
        assertTrue(resultado2.isPresent());
        assertEquals("João Silva", resultado1.get().getNome());
        assertEquals("Maria Silva", resultado2.get().getNome());
    }

    @Test
    @DisplayName("Deve chamar repositório com ID correto")
    void testChamarRepositorioComIdCorreto() {
        // Arrange
        when(usuarioRepository.obterPorId(1))
                .thenReturn(Optional.of(usuario));

        // Act
        useCase.run(1);

        // Assert
        verify(usuarioRepository).obterPorId(1);
    }

    @Test
    @DisplayName("Deve validar ID antes de consultar repositório")
    void testValidarIdAntesDeChamarRepositorio() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0);
        });

        verify(usuarioRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve obter usuário inativo por ID")
    void testObterUsuarioInativoPorId() {
        // Arrange
        Usuario usuarioInativo = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senha123")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(false)
                .build();

        when(usuarioRepository.obterPorId(1))
                .thenReturn(Optional.of(usuarioInativo));

        // Act
        Optional<Usuario> resultado = useCase.run(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertFalse(resultado.get().getAtivo());
    }

    @Test
    @DisplayName("Deve retornar Optional com usuário válido")
    void testRetornarOptionalComUsuarioValido() {
        // Arrange
        when(usuarioRepository.obterPorId(1))
                .thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run(1);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isPresent());
    }

}
