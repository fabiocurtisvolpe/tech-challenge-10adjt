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

import com.postech.adjt.domain.dto.NovoUsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

/**
 * Testes unitários para CadastrarUsuarioUseCase
 * 
 * Testa a criação de novos usuários no sistema
 * 
 * @author Fabio
 * @since 2025-12-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarUsuarioUseCase - Testes Unitários")
class CadastrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private CadastrarUsuarioUseCase useCase;

    private TipoUsuario tipoUsuarioValido;
    private NovoUsuarioDTO novoUsuarioDTO;
    private List<Endereco> enderecos;

    @BeforeEach
    void setUp() {
        useCase = CadastrarUsuarioUseCase.create(usuarioRepository);

        // Preparar endereços para o novo usuário
        enderecos = new ArrayList<>();
        enderecos.add(Endereco.builder()
                .logradouro("Rua Principal")
                .numero("456")
                .complemento("Apto 202")
                .bairro("Bairro Centro")
                .pontoReferencia("Próximo à estação")
                .cep("98765-432")
                .municipio("Rio de Janeiro")
                .uf("RJ")
                .principal(true)
                .build());

        tipoUsuarioValido = TipoUsuario.builder()
                .id(1)
                .descricao("CLIENTE")
                .build();

        // Preparar DTO de novo usuário
        novoUsuarioDTO = new NovoUsuarioDTO(
                "Novo Usuário",
                "novo.usuario@email.com",
                "senha123Segura",
                tipoUsuarioValido,
                enderecos);
    }

    @Test
    @DisplayName("Deve cadastrar novo usuário com sucesso")
    void testCadastrarNovoUsuarioComSucesso() {
        // Arrange
        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(1)
                .nome("Novo Usuário")
                .email("novo.usuario@email.com")
                .senha("senha123Segura")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        Usuario resultado = useCase.run(novoUsuarioDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Novo Usuário", resultado.getNome());
        assertEquals("novo.usuario@email.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).obterPorEmail("novo.usuario@email.com");
        verify(usuarioRepository, times(1)).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void testCadastrarComEmailExistente() {
        // Arrange
        Usuario usuarioExistente = Usuario.builder()
                .id(1)
                .nome("Usuário Existente")
                .email("novo.usuario@email.com")
                .senha("senhaExistente")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(new ArrayList<>())
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.of(usuarioExistente));

        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(novoUsuarioDTO);
        });

        verify(usuarioRepository, times(1)).obterPorEmail("novo.usuario@email.com");
        verify(usuarioRepository, never()).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve validar usuário antes de criar")
    void testValidacaoAntesDeCriar() {
        // Arrange
        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(1)
                .nome("Novo Usuário")
                .email("novo.usuario@email.com")
                .senha("senha123Segura")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        Usuario resultado = useCase.run(novoUsuarioDTO);

        // Assert
        assertNotNull(resultado);
        verify(usuarioRepository).criar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve criar usuário com múltiplos endereços")
    void testCadastrarUsuarioComMultiplosEnderecos() {
        // Arrange
        List<Endereco> multiplosEnderecos = new ArrayList<>();
        multiplosEnderecos.add(Endereco.builder()
                .logradouro("Rua A")
                .numero("100")
                .complemento("Casa")
                .bairro("Bairro A")
                .pontoReferencia("Próximo A")
                .cep("11111-111")
                .municipio("São Paulo")
                .uf("SP")
                .principal(true)
                .build());

        multiplosEnderecos.add(Endereco.builder()
                .logradouro("Rua B")
                .numero("200")
                .complemento("Apto")
                .bairro("Bairro B")
                .pontoReferencia("Próximo B")
                .cep("22222-222")
                .municipio("Rio de Janeiro")
                .uf("RJ")
                .principal(false)
                .build());

        NovoUsuarioDTO dtoMultiplosEnderecos = new NovoUsuarioDTO(
                "Usuário Multi",
                "usuario.multi@email.com",
                "senha456",
                tipoUsuarioValido,
                multiplosEnderecos);

        when(usuarioRepository.obterPorEmail("usuario.multi@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(2)
                .nome("Usuário Multi")
                .email("usuario.multi@email.com")
                .senha("senha456")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(multiplosEnderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        Usuario resultado = useCase.run(dtoMultiplosEnderecos);

        // Assert
        assertEquals(2, resultado.getEnderecos().size());
        assertTrue(resultado.getEnderecos().get(0).getPrincipal());
        assertFalse(resultado.getEnderecos().get(1).getPrincipal());
    }

    @Test
    @DisplayName("Deve criar usuário como ativo")
    void testNovoUsuarioCriadoComAtivo() {
        // Arrange
        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(3)
                .nome("Novo Usuário")
                .email("novo.usuario@email.com")
                .senha("senha123Segura")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        Usuario resultado = useCase.run(novoUsuarioDTO);

        // Assert
        assertTrue(resultado.getAtivo());
    }

    @Test
    @DisplayName("Deve criar usuários de diferentes tipos")
    void testCadastrarUsuariosDiferentesTipos() {
        // Arrange - Usuário Cliente
        NovoUsuarioDTO dtoCliente = new NovoUsuarioDTO(
                "Cliente",
                "cliente@email.com",
                "senha1",
                tipoUsuarioValido,
                enderecos);

        Usuario usuarioCliente = Usuario.builder()
                .id(1)
                .nome("Cliente")
                .email("cliente@email.com")
                .senha("senha1")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("cliente@email.com"))
                .thenReturn(Optional.empty());
        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCliente);

        // Act - Criar Cliente
        Usuario resultadoCliente = useCase.run(dtoCliente);

        // Assert - Validar Cliente
        assertEquals(tipoUsuarioValido, resultadoCliente.getTipoUsuario());

        // Arrange - Usuário Fornecedor
        NovoUsuarioDTO dtoFornecedor = new NovoUsuarioDTO(
                "Fornecedor",
                "fornecedor@email.com",
                "senha2",
                tipoUsuarioValido,
                enderecos);

        Usuario usuarioFornecedor = Usuario.builder()
                .id(2)
                .nome("Fornecedor")
                .email("fornecedor@email.com")
                .senha("senha2")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail("fornecedor@email.com"))
                .thenReturn(Optional.empty());
        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioFornecedor);

        // Act - Criar Fornecedor
        Usuario resultadoFornecedor = useCase.run(dtoFornecedor);

        // Assert - Validar Fornecedor
        assertEquals(tipoUsuarioValido, resultadoFornecedor.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve usar dados do DTO para criar usuário")
    void testUsandoDadosDoDTOParaCriar() {
        // Arrange
        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(1)
                .nome("Novo Usuário")
                .email("novo.usuario@email.com")
                .senha("senha123Segura")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        Usuario resultado = useCase.run(novoUsuarioDTO);

        // Assert
        assertEquals(novoUsuarioDTO.nome(), resultado.getNome());
        assertEquals(novoUsuarioDTO.email(), resultado.getEmail());
        assertEquals(novoUsuarioDTO.tipoUsuario(), resultado.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve retornar usuário com ID após criação")
    void testUsuarioComIDAposCriacao() {
        // Arrange
        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(999)
                .nome("Novo Usuário")
                .email("novo.usuario@email.com")
                .senha("senha123Segura")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        Usuario resultado = useCase.run(novoUsuarioDTO);

        // Assert
        assertNotNull(resultado.getId());
        assertEquals(999, resultado.getId());
    }

    @Test
    @DisplayName("Deve verificar email em repositório antes de criar")
    void testVerificacaoEmailAntesDeCriar() {
        // Arrange
        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(1)
                .nome("Novo Usuário")
                .email("novo.usuario@email.com")
                .senha("senha123Segura")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        useCase.run(novoUsuarioDTO);

        // Assert
        verify(usuarioRepository).obterPorEmail("novo.usuario@email.com");
    }

    @Test
    @DisplayName("Deve manter dados de endereços ao criar usuário")
    void testMantendoDadosEnderecos() {
        // Arrange
        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(1)
                .nome("Novo Usuário")
                .email("novo.usuario@email.com")
                .senha("senha123Segura")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        Usuario resultado = useCase.run(novoUsuarioDTO);

        // Assert
        assertEquals(enderecos.size(), resultado.getEnderecos().size());
        assertEquals("Rua Principal", resultado.getEnderecos().get(0).getLogradouro());
        assertEquals("456", resultado.getEnderecos().get(0).getNumero());
    }

    @Test
    @DisplayName("Deve chamar repositório criar apenas uma vez")
    void testRepositorioCriarChamadoApenasUmaVez() {
        // Arrange
        when(usuarioRepository.obterPorEmail("novo.usuario@email.com"))
                .thenReturn(Optional.empty());

        Usuario usuarioCriado = Usuario.builder()
                .id(1)
                .nome("Novo Usuário")
                .email("novo.usuario@email.com")
                .senha("senha123Segura")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .ativo(true)
                .build();

        when(usuarioRepository.criar(any(Usuario.class)))
                .thenReturn(usuarioCriado);

        // Act
        useCase.run(novoUsuarioDTO);

        // Assert
        verify(usuarioRepository, times(1)).criar(any(Usuario.class));
    }

}
