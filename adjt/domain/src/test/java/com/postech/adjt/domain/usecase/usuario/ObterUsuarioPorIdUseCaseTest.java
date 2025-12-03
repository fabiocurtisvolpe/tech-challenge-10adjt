package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterUsuarioPorIdUseCase - Testes Unitários")
class ObterUsuarioPorIdUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private ObterUsuarioPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterUsuarioPorIdUseCase.create(usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar usuário quando encontrado por ID")
    void testObterUsuarioPorIdComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        enderecos.add(endereco);

        Usuario usuario = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );
        usuario.setId(1);

        when(usuarioRepository.obterPorId(1)).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        assertEquals("joao@email.com", resultado.get().getEmail());
        assertEquals(Integer.valueOf(1), resultado.get().getId());
        verify(usuarioRepository, times(1)).obterPorId(1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é null")
    void testLancarExcecaoQuandoIdEhNulo() {
        // Act & Assert
        NotificacaoException exception = assertThrows(
            NotificacaoException.class,
            () -> useCase.run(null)
        );

        assertEquals(MensagemUtil.ID_NULO, exception.getMessage());
        verify(usuarioRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é zero")
    void testLancarExcecaoQuandoIdEhZero() {
        // Act & Assert
        NotificacaoException exception = assertThrows(
            NotificacaoException.class,
            () -> useCase.run(0)
        );

        assertEquals(MensagemUtil.ID_NULO, exception.getMessage());
        verify(usuarioRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é negativo")
    void testLancarExcecaoQuandoIdEhNegativo() {
        // Act & Assert
        NotificacaoException exception = assertThrows(
            NotificacaoException.class,
            () -> useCase.run(-1)
        );

        assertEquals(MensagemUtil.ID_NULO, exception.getMessage());
        verify(usuarioRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é encontrado")
    void testLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.obterPorId(999)).thenReturn(Optional.empty());

        // Act & Assert
        NotificacaoException exception = assertThrows(
            NotificacaoException.class,
            () -> useCase.run(999)
        );

        assertEquals(MensagemUtil.USUARIO_NAO_ENCONTRADO, exception.getMessage());
        verify(usuarioRepository, times(1)).obterPorId(999);
    }

    @Test
    @DisplayName("Deve retornar usuário com todos os dados preservados")
    void testRetornandoUsuarioComTodosDadosPreservados() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua B", "456", "Apt 202", "Bairro Y", "Ponto Ref", "23456-789", "Rio de Janeiro", "RJ", false, null);
        enderecos.add(endereco);

        Usuario usuario = new Usuario(
            "Maria Santos",
            "maria@email.com",
            "senhaSegura",
            TipoUsuarioEnum.DONO_RESTAURANTE,
            enderecos
        );
        usuario.setId(5);

        when(usuarioRepository.obterPorId(5)).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run(5);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Maria Santos", resultado.get().getNome());
        assertEquals("maria@email.com", resultado.get().getEmail());
        assertEquals("senhaSegura", resultado.get().getSenha());
        assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, resultado.get().getTipoUsuario());
        assertEquals(Integer.valueOf(5), resultado.get().getId());
        assertEquals(1, resultado.get().getEnderecos().size());
        verify(usuarioRepository, times(1)).obterPorId(5);
    }

    @Test
    @DisplayName("Deve retornar usuário com múltiplos endereços")
    void testRetornandoUsuarioComMultiplosEnderecos() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco1 = new Endereco("Rua C", "789", "Apt 303", "Bairro Z", "Ponto Ref 1", "34567-890", "Brasília", "DF", true, null);
        Endereco endereco2 = new Endereco("Rua D", "321", "Apt 404", "Bairro W", "Ponto Ref 2", "45678-901", "São Paulo", "SP", false, null);
        enderecos.add(endereco1);
        enderecos.add(endereco2);

        Usuario usuario = new Usuario(
            "Pedro Costa",
            "pedro@email.com",
            "senha456",
            TipoUsuarioEnum.FORNECEDOR,
            enderecos
        );
        usuario.setId(10);

        when(usuarioRepository.obterPorId(10)).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run(10);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(2, resultado.get().getEnderecos().size());
        assertEquals("Pedro Costa", resultado.get().getNome());
        verify(usuarioRepository, times(1)).obterPorId(10);
    }

    @Test
    @DisplayName("Deve retornar usuário com tipo PRESTADOR_SERVICO")
    void testRetornandoUsuarioPrestadorServico() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua E", "654", "Apt 505", "Bairro V", "Ponto Ref", "56789-012", "Salvador", "BA", false, null);
        enderecos.add(endereco);

        Usuario usuario = new Usuario(
            "Ana Silva",
            "ana@email.com",
            "senha789",
            TipoUsuarioEnum.PRESTADOR_SERVICO,
            enderecos
        );
        usuario.setId(15);

        when(usuarioRepository.obterPorId(15)).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = useCase.run(15);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(TipoUsuarioEnum.PRESTADOR_SERVICO, resultado.get().getTipoUsuario());
        assertEquals(Integer.valueOf(15), resultado.get().getId());
        verify(usuarioRepository, times(1)).obterPorId(15);
    }
}
