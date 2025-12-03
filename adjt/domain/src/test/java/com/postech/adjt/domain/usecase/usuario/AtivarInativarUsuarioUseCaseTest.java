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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtivarInativarUsuarioUseCase - Testes Unitários")
class AtivarInativarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private AtivarInativarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarUsuarioUseCase.create(usuarioRepository);
    }

    @Test
    @DisplayName("Deve ativar um usuário com sucesso")
    void testAtivarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        enderecos.add(endereco);
        
        Usuario usuarioExistente = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );
        usuarioExistente.setId(1);

        Usuario usuarioAtivado = Usuario.atualizar(
            usuarioExistente.getId(),
            usuarioExistente.getNome(),
            usuarioExistente.getEmail(),
            usuarioExistente.getSenha(),
            usuarioExistente.getTipoUsuario(),
            usuarioExistente.getEnderecos(),
            true
        );

        when(usuarioRepository.obterPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtivado);

        // Act
        Usuario resultado = useCase.run("joao@email.com", true);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).obterPorEmail("joao@email.com");
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve inativar um usuário com sucesso")
    void testInativarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua B", "456", "Apt 202", "Bairro Y", "Ponto Ref", "23456-789", "Rio de Janeiro", "RJ", false, null);
        enderecos.add(endereco);
        
        Usuario usuarioExistente = new Usuario(
            "Maria Santos",
            "maria@email.com",
            "senha456",
            TipoUsuarioEnum.DONO_RESTAURANTE,
            enderecos
        );
        usuarioExistente.setId(2);

        Usuario usuarioInativado = Usuario.atualizar(
            usuarioExistente.getId(),
            usuarioExistente.getNome(),
            usuarioExistente.getEmail(),
            usuarioExistente.getSenha(),
            usuarioExistente.getTipoUsuario(),
            usuarioExistente.getEnderecos(),
            false
        );

        when(usuarioRepository.obterPorEmail("maria@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioInativado);

        // Act
        Usuario resultado = useCase.run("maria@email.com", false);

        // Assert
        assertNotNull(resultado);
        assertEquals("Maria Santos", resultado.getNome());
        assertEquals("maria@email.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).obterPorEmail("maria@email.com");
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void testLancarExcecaoQuandoUsuarioNaoExiste() {
        // Arrange
        when(usuarioRepository.obterPorEmail("inexistente@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        NotificacaoException exception = assertThrows(
            NotificacaoException.class,
            () -> useCase.run("inexistente@email.com", true)
        );

        assertEquals(MensagemUtil.USUARIO_NAO_ENCONTRADO, exception.getMessage());
        verify(usuarioRepository, times(1)).obterPorEmail("inexistente@email.com");
        verify(usuarioRepository, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve manter dados do usuário ao ativar")
    void testMantendoTodosDadosAoAtivar() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        enderecos.add(endereco);

        Usuario usuarioExistente = new Usuario(
            "Pedro Costa",
            "pedro@email.com",
            "senhaSegura",
            TipoUsuarioEnum.FORNECEDOR,
            enderecos
        );
        usuarioExistente.setId(3);

        Usuario usuarioAtivado = Usuario.atualizar(
            usuarioExistente.getId(),
            usuarioExistente.getNome(),
            usuarioExistente.getEmail(),
            usuarioExistente.getSenha(),
            usuarioExistente.getTipoUsuario(),
            usuarioExistente.getEnderecos(),
            true
        );

        when(usuarioRepository.obterPorEmail("pedro@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtivado);

        // Act
        Usuario resultado = useCase.run("pedro@email.com", true);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pedro Costa", resultado.getNome());
        assertEquals("pedro@email.com", resultado.getEmail());
        assertEquals("senhaSegura", resultado.getSenha());
        assertEquals(TipoUsuarioEnum.FORNECEDOR, resultado.getTipoUsuario());
        assertEquals(1, resultado.getEnderecos().size());
        verify(usuarioRepository, times(1)).obterPorEmail("pedro@email.com");
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve preservar ID do usuário ao alternar ativo")
    void testPreservandoIdAoAlternarAtivo() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua C", "789", "Apt 303", "Bairro Z", "Ponto Ref", "34567-890", "Brasília", "DF", false, null);
        enderecos.add(endereco);
        
        Usuario usuarioExistente = new Usuario(
            "Ana Silva",
            "ana@email.com",
            "senha789",
            TipoUsuarioEnum.PRESTADOR_SERVICO,
            enderecos
        );
        usuarioExistente.setId(5);

        Usuario usuarioAtualizado = Usuario.atualizar(
            usuarioExistente.getId(),
            usuarioExistente.getNome(),
            usuarioExistente.getEmail(),
            usuarioExistente.getSenha(),
            usuarioExistente.getTipoUsuario(),
            usuarioExistente.getEnderecos(),
            false
        );

        when(usuarioRepository.obterPorEmail("ana@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = useCase.run("ana@email.com", false);

        // Assert
        assertNotNull(resultado);
        assertEquals(Integer.valueOf(5), resultado.getId());
        verify(usuarioRepository, times(1)).obterPorEmail("ana@email.com");
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }
}
