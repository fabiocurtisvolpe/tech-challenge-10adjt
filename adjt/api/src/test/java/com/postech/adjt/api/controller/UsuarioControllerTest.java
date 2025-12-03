package com.postech.adjt.api.controller;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.AtualizaUsuarioPayLoad;
import com.postech.adjt.api.payload.EnderecoPayLoad;
import com.postech.adjt.api.payload.NovoUsuarioPayLoad;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.usecase.usuario.AtivarInativarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarSenhaUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.CadastrarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorEmailUseCase;
import com.postech.adjt.domain.usecase.usuario.PaginadoUsuarioUseCase;

/**
 * Testes unitários para UsuarioController
 * 
 * Testa os endpoints da API de usuários
 * 
 * @author Fabio
 * @since 2025-12-03
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioController - Testes Unitários")
class UsuarioControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @Mock
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @Mock
    private AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase;

    @Mock
    private ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase;

    @Mock
    private PaginadoUsuarioUseCase paginadoUsuarioUseCase;

    @Mock
    private AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase;

    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioController = new UsuarioController(
                passwordEncoder,
                ativarInativarUsuarioUseCase,
                cadastrarUsuarioUseCase,
                atualizarUsuarioUseCase,
                atualizarSenhaUsuarioUseCase,
                obterUsuarioPorEmailUseCase,
                paginadoUsuarioUseCase);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void testCriarUsuarioComSucesso() {
        // Arrange
        NovoUsuarioPayLoad payload = new NovoUsuarioPayLoad();
        payload.setNome("João Silva");
        payload.setEmail("joao@email.com");
        payload.setSenha("senha123");
        payload.setTipoUsuario(TipoUsuarioEnum.CLIENTE);

        EnderecoPayLoad endereco = new EnderecoPayLoad();
        endereco.setLogradouro("Rua A");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCep("12345-678");
        endereco.setMunicipio("São Paulo");
        endereco.setUf("SP");
        endereco.setPrincipal(true);

        payload.setEnderecos(List.of(endereco));

        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", null, "Centro", null,
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuarioCriado = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(passwordEncoder.encode("senha123")).thenReturn("senha_encriptada");
        when(cadastrarUsuarioUseCase.run(any())).thenReturn(usuarioCriado);

        // Act
        UsuarioRespostaDTO resultado = usuarioController.criar(payload);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(cadastrarUsuarioUseCase, times(1)).run(any());
    }

    @Test
    @DisplayName("Deve buscar usuário por email com sucesso")
    void testBuscarUsuarioPorEmailComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua A", "123", null, "Centro", null,
                "12345-678", "São Paulo", "SP", true, null));

        Usuario usuario = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(obterUsuarioPorEmailUseCase.run("joao@email.com")).thenReturn(Optional.of(usuario));

        // Act
        UsuarioRespostaDTO resultado = usuarioController.buscar("joao@email.com");

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(obterUsuarioPorEmailUseCase, times(1)).run("joao@email.com");
    }

    @Test
    @DisplayName("Deve retornar nulo quando usuário não existe")
    void testBuscarUsuarioNaoEncontrado() {
        // Arrange
        when(obterUsuarioPorEmailUseCase.run("inexistente@email.com")).thenReturn(Optional.empty());

        // Act
        UsuarioRespostaDTO resultado = usuarioController.buscar("inexistente@email.com");

        // Assert
        assertNull(resultado);
        verify(obterUsuarioPorEmailUseCase, times(1)).run("inexistente@email.com");
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void testAtualizarUsuarioComSucesso() {
        // Arrange
        AtualizaUsuarioPayLoad payload = new AtualizaUsuarioPayLoad();
        payload.setNome("João Silva Atualizado");
        payload.setEmail("joao.atualizado@email.com");

        EnderecoPayLoad endereco = new EnderecoPayLoad();
        endereco.setLogradouro("Rua B");
        endereco.setNumero("456");
        endereco.setBairro("Zona Sul");
        endereco.setCep("98765-432");
        endereco.setMunicipio("São Paulo");
        endereco.setUf("SP");
        endereco.setPrincipal(true);

        payload.setEnderecos(List.of(endereco));

        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua B", "456", null, "Zona Sul", null,
                "98765-432", "São Paulo", "SP", true, null));

        Usuario usuarioAtualizado = new Usuario(
                "João Silva Atualizado",
                "joao.atualizado@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(atualizarUsuarioUseCase.run(any())).thenReturn(usuarioAtualizado);

        // Act
        UsuarioRespostaDTO resultado = usuarioController.atualizar(payload);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva Atualizado", resultado.getNome());
        assertEquals("joao.atualizado@email.com", resultado.getEmail());
        verify(atualizarUsuarioUseCase, times(1)).run(any());
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void testAtualizarSenhaComSucesso() {
        // Arrange
        com.postech.adjt.api.payload.TrocarSenhaUsuarioPayLoad payload = 
            new com.postech.adjt.api.payload.TrocarSenhaUsuarioPayLoad();
        payload.setEmail("joao@email.com");
        payload.setSenha("novaSenha123");

        List<Endereco> enderecos = new ArrayList<>();
        Usuario usuarioAtualizado = new Usuario(
                "João Silva",
                "joao@email.com",
                "novaSenha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(passwordEncoder.encode("novaSenha123")).thenReturn("novaSenha_encriptada");
        when(atualizarSenhaUsuarioUseCase.run(any())).thenReturn(usuarioAtualizado);

        // Act
        UsuarioRespostaDTO resultado = usuarioController.atualizarSenha(payload);

        // Assert
        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getEmail());
        verify(atualizarSenhaUsuarioUseCase, times(1)).run(any());
    }

    @Test
    @DisplayName("Deve ativar usuário com sucesso")
    void testAtivarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Usuario usuarioAtivado = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(ativarInativarUsuarioUseCase.run("joao@email.com", true)).thenReturn(usuarioAtivado);

        // Act
        UsuarioRespostaDTO resultado = usuarioController.ativar("joao@email.com");

        // Assert
        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getEmail());
        verify(ativarInativarUsuarioUseCase, times(1)).run("joao@email.com", true);
    }

    @Test
    @DisplayName("Deve desativar usuário com sucesso")
    void testDesativarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Usuario usuarioDesativado = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        when(ativarInativarUsuarioUseCase.run("joao@email.com", false)).thenReturn(usuarioDesativado);

        // Act
        UsuarioRespostaDTO resultado = usuarioController.desativar("joao@email.com");

        // Assert
        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getEmail());
        verify(ativarInativarUsuarioUseCase, times(1)).run("joao@email.com", false);
    }

    @Test
    @DisplayName("Deve listar usuários paginados com sucesso")
    void testListarUsuariosPaginadosComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Usuario usuario = new Usuario(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos);

        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
                List.of(usuario),
                0,
                10,
                1);

        com.postech.adjt.api.payload.PaginacaoPayLoad paginacao = new com.postech.adjt.api.payload.PaginacaoPayLoad();
        paginacao.setPage(0);
        paginacao.setSize(10);

        when(paginadoUsuarioUseCase.run(0, 10, null, null)).thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<UsuarioRespostaDTO> resultadoPaginado = usuarioController.paginado(paginacao);

        // Assert
        assertNotNull(resultadoPaginado);
        assertEquals(1, resultadoPaginado.getContent().size());
        verify(paginadoUsuarioUseCase, times(1)).run(0, 10, null, null);
    }
}
