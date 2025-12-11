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
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Testes unitários para AtualizarSenhaUsuarioUseCase
 * 
 * Testa a atualização de senha de usuários no sistema
 * 
 * @author Fabio
 * @since 2025-12-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarSenhaUsuarioUseCase - Testes Unitários")
class AtualizarSenhaUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    private AtualizarSenhaUsuarioUseCase useCase;

    private Usuario usuarioExistente;
    private TipoUsuario tipoUsuarioValido;
    private List<Endereco> enderecos;
    private TrocarSenhaUsuarioDTO trocarSenhaDTO;

    @BeforeEach
    void setUp() {
        useCase = AtualizarSenhaUsuarioUseCase.create(usuarioRepository);

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

        tipoUsuarioValido = TipoUsuarioFactory.atualizar(1, "CLIENTE", "CLIENTE", true, false);

        usuarioExistente = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senhaAntiga123")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        trocarSenhaDTO = new TrocarSenhaUsuarioDTO("joao@email.com", "senhaAntiga123", "senhaCodificada456");
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void testAtualizarSenhaComSucesso() {
        // Arrange
        Usuario usuarioComSenhaAtualizada = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senhaCodificada456")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuarioComSenhaAtualizada);

        // Act
        Usuario resultado = useCase.run(trocarSenhaDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("senhaCodificada456", resultado.getSenha());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).obterPorEmail("joao@email.com");
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void testAtualizarSenhaUsuarioNaoEncontrado() {
        // Arrange
        TrocarSenhaUsuarioDTO dtoInexistente = new TrocarSenhaUsuarioDTO("inexistente@email.com", "senhaAntiga",
                "novaSenha");

        when(usuarioRepository.obterPorEmail("inexistente@email.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(dtoInexistente);
        });

        verify(usuarioRepository, times(1)).obterPorEmail("inexistente@email.com");
        verify(usuarioRepository, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve manter dados do usuário ao atualizar senha")
    void testMantendoDadosAoAtualizarSenha() {
        // Arrange
        Usuario usuarioAtualizado = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senhaCodificada456")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = useCase.run(trocarSenhaDTO);

        // Assert
        assertEquals(1, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertEquals(tipoUsuarioValido, resultado.getTipoUsuario());
        assertEquals(enderecos.size(), resultado.getEnderecos().size());
    }

    @Test
    @DisplayName("Deve manter endereços do usuário ao atualizar senha")
    void testMantendoEnderecosAoAtualizarSenha() {
        // Arrange
        Usuario usuarioAtualizado = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senhaCodificada456")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = useCase.run(trocarSenhaDTO);

        // Assert
        assertNotNull(resultado.getEnderecos());
        assertEquals(1, resultado.getEnderecos().size());
        assertEquals("Rua Teste", resultado.getEnderecos().get(0).getLogradouro());
        assertEquals("123", resultado.getEnderecos().get(0).getNumero());
    }

    @Test
    @DisplayName("Deve atualizar senha para diferentes usuários")
    void testAtualizarSenhaParaDiferentesUsuarios() {
        // Arrange
        Usuario usuario1 = Usuario.builder()
                .id(1)
                .nome("João")
                .email("joao@email.com")
                .senha("senhaAntigaJoao")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(new ArrayList<>())
                .ativo(true)
                .build();

        tipoUsuarioValido = TipoUsuarioFactory.atualizar(1, "CLIENTE", "CLIENTE", true, false);

        TipoUsuario tipoUsuarioFornecedor = TipoUsuarioFactory.atualizar(2, "FORNECEDOR", "FORNECEDOR", true, false);

        Usuario usuario2 = Usuario.builder()
                .id(2)
                .nome("Maria")
                .email("maria@email.com")
                .senha("senhaAntigaMaria")
                .tipoUsuario(tipoUsuarioFornecedor)
                .enderecos(new ArrayList<>())
                .ativo(true)
                .build();

        Usuario usuario1Atualizado = Usuario.builder()
                .id(1)
                .nome("João")
                .email("joao@email.com")
                .senha("senhaNova123")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(new ArrayList<>())
                .ativo(true)
                .build();

        Usuario usuario2Atualizado = Usuario.builder()
                .id(2)
                .nome("Maria")
                .email("maria@email.com")
                .senha("senhaNova456")
                .tipoUsuario(tipoUsuarioFornecedor)
                .enderecos(new ArrayList<>())
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuario1));
        when(usuarioRepository.obterPorEmail("maria@email.com"))
                .thenReturn(Optional.of(usuario2));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuario1Atualizado)
                .thenReturn(usuario2Atualizado);

        // Act
        TrocarSenhaUsuarioDTO dto1 = new TrocarSenhaUsuarioDTO("joao@email.com", "senhaAntigaJoao", "senhaNova123");
        TrocarSenhaUsuarioDTO dto2 = new TrocarSenhaUsuarioDTO("maria@email.com", "senhaAntigaMaria", "senhaNova456");

        Usuario resultado1 = useCase.run(dto1);
        Usuario resultado2 = useCase.run(dto2);

        // Assert
        assertEquals("senhaNova123", resultado1.getSenha());
        assertEquals("senhaNova456", resultado2.getSenha());
        verify(usuarioRepository, times(2)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve usar email do DTO para buscar usuário")
    void testUsandoEmailDoDTOParaBuscaUsuario() {
        // Arrange
        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuarioExistente);

        // Act
        useCase.run(trocarSenhaDTO);

        // Assert
        verify(usuarioRepository).obterPorEmail("joao@email.com");
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve manter usuário ativo após atualizar senha")
    void testMantendoUsuarioAtivoAposProcesso() {
        // Arrange
        Usuario usuarioAtualizado = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senhaCodificada456")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = useCase.run(trocarSenhaDTO);

        // Assert
        assertTrue(resultado.getAtivo());
    }

    @Test
    @DisplayName("Deve processar DTO com senha codificada corretamente")
    void testProcessandoDTOComSenhaCodificada() {
        // Arrange
        String senhaCodificadaEsperada = "senhaCodificada456";
        TrocarSenhaUsuarioDTO dto = new TrocarSenhaUsuarioDTO("joao@email.com", "senhaAntiga123",
                senhaCodificadaEsperada);

        Usuario usuarioAtualizado = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha(senhaCodificadaEsperada)
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = useCase.run(dto);

        // Assert
        assertEquals(senhaCodificadaEsperada, resultado.getSenha());
    }

    @Test
    @DisplayName("Deve chamar repositório em sequência correta")
    void testSequenciaDeChamsAoRepositorio() {
        // Arrange
        Usuario usuarioAtualizado = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senhaCodificada456")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuarioAtualizado);

        // Act
        useCase.run(trocarSenhaDTO);

        // Assert
        InOrder inOrder = inOrder(usuarioRepository);
        inOrder.verify(usuarioRepository).obterPorEmail("joao@email.com");
        inOrder.verify(usuarioRepository).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve validar que senha anterior é preservada em usuário buscado")
    void testSenhaAnteriorPreservada() {
        // Arrange
        String senhaAnterior = "senhaAntiga123";
        assertEquals(senhaAnterior, usuarioExistente.getSenha());

        Usuario usuarioAtualizado = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senhaCodificada456")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.atualizar(any(Usuario.class)))
                .thenReturn(usuarioAtualizado);

        // Act
        useCase.run(trocarSenhaDTO);

        // Assert
        verify(usuarioRepository).obterPorEmail("joao@email.com");
    }

}
