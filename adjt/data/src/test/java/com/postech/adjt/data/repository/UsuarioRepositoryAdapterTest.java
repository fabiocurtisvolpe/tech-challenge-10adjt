package com.postech.adjt.data.repository;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.data.repository.jpa.JpaDataUsuarioRepository;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.entidade.TipoUsuario;

/**
 * Testes unitários para UsuarioRepositoryAdapter
 * 
 * Testa as operações de repositório com mocks do Spring Data
 * 
 * @author Fabio
 * @since 2025-12-03
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioRepositoryAdapter - Testes Unitários")
@SuppressWarnings({"unchecked", "null"})
class UsuarioRepositoryAdapterTest {

    @Mock
    private JpaDataUsuarioRepository springDataUsuarioRepository;

    private UsuarioRepositoryAdapter usuarioRepositoryAdapter;
    
    private TipoUsuario tipoUsuario;
    private TipoUsuarioEntidade tipoUsuarioEntidade;

    @BeforeEach
    void setUp() {
        usuarioRepositoryAdapter = new UsuarioRepositoryAdapter(springDataUsuarioRepository);
        
        tipoUsuario = TipoUsuarioFactory.tipoUsuario(1, "CLIENTE", "CLIENTE", true, false);
        

        tipoUsuarioEntidade = new TipoUsuarioEntidade();
        tipoUsuarioEntidade.setId(1);
        tipoUsuarioEntidade.setNome("CLIENTE");
        tipoUsuarioEntidade.setDescricao("CLIENTE");
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void testCriarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(Endereco.builder()
                .logradouro("Rua A")
                .numero("123")
                .complemento("Apto 10")
                .bairro("Centro")
                .pontoReferencia("Perto da padaria")
                .cep("12345-678")
                .municipio("São Paulo")
                .uf("SP")
                .principal(true)
                .build());

        Usuario usuario = Usuario.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senha123")
                .tipoUsuario(tipoUsuario)
                .enderecos(enderecos)
                .build();

        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("João Silva");
        entidade.setEmail("joao@email.com");
        entidade.setSenha("senha123");
        entidade.setTipoUsuario(tipoUsuarioEntidade);
        entidade.setAtivo(true);

        when(springDataUsuarioRepository.save(any(UsuarioEntidade.class))).thenReturn(entidade);

        // Act
        Usuario resultado = usuarioRepositoryAdapter.criar(usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(springDataUsuarioRepository, times(1)).save(any(UsuarioEntidade.class));
    }

    @Test
    @DisplayName("Deve obter usuário por ID com sucesso")
    void testObterPorIdComSucesso() {
        // Arrange
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("João Silva");
        entidade.setEmail("joao@email.com");
        entidade.setTipoUsuario(tipoUsuarioEntidade);
        entidade.setAtivo(true);

        when(springDataUsuarioRepository.findById(1)).thenReturn(Optional.of(entidade));

        // Act
        Optional<Usuario> resultado = usuarioRepositoryAdapter.obterPorId(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        verify(springDataUsuarioRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando usuário não existe por ID")
    void testObterPorIdNaoEncontrado() {
        // Arrange
        when(springDataUsuarioRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioRepositoryAdapter.obterPorId(999);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(springDataUsuarioRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Deve obter usuário por email com sucesso")
    void testObterPorEmailComSucesso() {
        // Arrange
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("João Silva");
        entidade.setEmail("joao@email.com");
        entidade.setTipoUsuario(tipoUsuarioEntidade);
        entidade.setAtivo(true);

        when(springDataUsuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(entidade));

        // Act
        Optional<Usuario> resultado = usuarioRepositoryAdapter.obterPorEmail("joao@email.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        verify(springDataUsuarioRepository, times(1)).findByEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando usuário não existe por email")
    void testObterPorEmailNaoEncontrado() {
        // Arrange
        when(springDataUsuarioRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioRepositoryAdapter.obterPorEmail("inexistente@email.com");

        // Assert
        assertTrue(resultado.isEmpty());
        verify(springDataUsuarioRepository, times(1)).findByEmail("inexistente@email.com");
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void testAtualizarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(Endereco.builder()
                .logradouro("Rua A")
                .numero("123")
                .complemento("Apto 10")
                .bairro("Centro")
                .pontoReferencia("Perto da padaria")
                .cep("12345-678")
                .municipio("São Paulo")
                .uf("SP")
                .principal(true)
                .build());

        Usuario usuario = Usuario.builder()
                .id(1)
                .nome("João Silva Atualizado")
                .email("joao.atualizado@email.com")
                .senha("senha123")
                .tipoUsuario(tipoUsuario)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("João Silva Atualizado");
        entidade.setEmail("joao.atualizado@email.com");
        entidade.setTipoUsuario(tipoUsuarioEntidade);
        entidade.setAtivo(true);

        when(springDataUsuarioRepository.save(any(UsuarioEntidade.class))).thenReturn(entidade);

        // Act
        Usuario resultado = usuarioRepositoryAdapter.atualizar(usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva Atualizado", resultado.getNome());
        assertEquals("joao.atualizado@email.com", resultado.getEmail());
        verify(springDataUsuarioRepository, times(1)).save(any(UsuarioEntidade.class));
    }

    @Test
    @DisplayName("Deve listar usuários paginados com sucesso")
    void testListarPaginadoComSucesso() {
        // Arrange
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("João Silva");
        entidade.setEmail("joao@email.com");
        entidade.setTipoUsuario(tipoUsuarioEntidade);
        entidade.setAtivo(true);

        Page<UsuarioEntidade> page = new PageImpl<>(
                List.of(entidade),
                PageRequest.of(0, 10),
                1);

        when(springDataUsuarioRepository.findAll(
                any(org.springframework.data.jpa.domain.Specification.class), 
                any(PageRequest.class))).thenReturn(page);

        // Act
        ResultadoPaginacaoDTO<Usuario> resultado = usuarioRepositoryAdapter.listarPaginado(0, 10, new ArrayList<>(), new ArrayList<>());

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(0, resultado.getPageNumber());
        assertEquals(10, resultado.getPageSize());
        assertEquals(1, resultado.getTotalElements());
        verify(springDataUsuarioRepository, times(1)).findAll(
                any(org.springframework.data.jpa.domain.Specification.class), 
                any(PageRequest.class));
    }

    @Test
    @DisplayName("Deve ativar/desativar usuário com sucesso")
    void testAtivarDesativarUsuarioComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Usuario usuario = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senha123")
                .tipoUsuario(tipoUsuario)
                .enderecos(enderecos)
                .ativo(false)
                .build();

        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("João Silva");
        entidade.setEmail("joao@email.com");
        entidade.setAtivo(false);

        when(springDataUsuarioRepository.save(any(UsuarioEntidade.class))).thenReturn(entidade);

        // Act
        Boolean resultado = usuarioRepositoryAdapter.ativarDesativar(usuario);

        // Assert
        assertTrue(resultado);
        verify(springDataUsuarioRepository, times(1)).save(any(UsuarioEntidade.class));
    }
}
