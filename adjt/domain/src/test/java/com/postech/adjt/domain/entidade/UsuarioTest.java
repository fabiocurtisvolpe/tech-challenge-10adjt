package com.postech.adjt.domain.entidade;

import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Usuario - Testes Unitários")
class UsuarioTest {

    private List<Endereco> enderecos;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        enderecos = new ArrayList<>();
        endereco = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        enderecos.add(endereco);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso usando factory method")
    void testCriarUsuarioComSucesso() {
        // Act
        Usuario usuario = Usuario.create(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Assert
        assertNotNull(usuario);
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        assertEquals(TipoUsuarioEnum.CLIENTE, usuario.getTipoUsuario());
        assertEquals(1, usuario.getEnderecos().size());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é null")
    void testLancarExcecaoQuandoNomeEhNulo() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                null,
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome está vazio")
    void testLancarExcecaoQuandoNomeVazio() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "   ",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
    void testLancarExcecaoQuandoNomeMuitoCurto() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "Jo",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é null")
    void testLancarExcecaoQuandoEmailEhNulo() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                null,
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando email está vazio")
    void testLancarExcecaoQuandoEmailVazio() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                "   ",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é inválido")
    void testLancarExcecaoQuandoEmailInvalido() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                "emailinvalido",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é null")
    void testLancarExcecaoQuandoSenhaEhNula() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                "joao@email.com",
                null,
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha está vazia")
    void testLancarExcecaoQuandoSenhaVazia() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                "joao@email.com",
                "",
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha tem menos de 6 caracteres")
    void testLancarExcecaoQuandoSenhaMuitoCurta() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                "joao@email.com",
                "sen12",
                TipoUsuarioEnum.CLIENTE,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário é null")
    void testLancarExcecaoQuandoTipoUsuarioEhNulo() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                "joao@email.com",
                "senha123",
                null,
                enderecos
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de endereços é null")
    void testLancarExcecaoQuandoEnderecosNulo() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                null
            )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de endereços está vazia")
    void testLancarExcecaoQuandoEnderecosVazia() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> Usuario.create(
                "João Silva",
                "joao@email.com",
                "senha123",
                TipoUsuarioEnum.CLIENTE,
                new ArrayList<>()
            )
        );
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void testAtualizarUsuarioComSucesso() {
        // Act
        Usuario usuario = Usuario.atualizar(
            1,
            "Maria Silva",
            "maria@email.com",
            "novaSenha",
            TipoUsuarioEnum.DONO_RESTAURANTE,
            enderecos,
            true
        );

        // Assert
        assertNotNull(usuario);
        assertEquals(Integer.valueOf(1), usuario.getId());
        assertEquals("Maria Silva", usuario.getNome());
        assertEquals("maria@email.com", usuario.getEmail());
        assertEquals("novaSenha", usuario.getSenha());
        assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, usuario.getTipoUsuario());
        assertTrue(usuario.getAtivo());
    }

    @Test
    @DisplayName("Deve criar usuário com construtor sem ID")
    void testCriarUsuarioComConstrutor() {
        // Act
        Usuario usuario = new Usuario(
            "Pedro Costa",
            "pedro@email.com",
            "senha456",
            TipoUsuarioEnum.FORNECEDOR,
            enderecos
        );

        // Assert
        assertNotNull(usuario);
        assertEquals("Pedro Costa", usuario.getNome());
        assertEquals("pedro@email.com", usuario.getEmail());
        assertEquals("senha456", usuario.getSenha());
        assertEquals(TipoUsuarioEnum.FORNECEDOR, usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve criar usuário com construtor com ID e ativo")
    void testCriarUsuarioComConstrutorCompleto() {
        // Act
        Usuario usuario = new Usuario(
            5,
            "Ana Santos",
            "ana@email.com",
            "senha789",
            TipoUsuarioEnum.PRESTADOR_SERVICO,
            enderecos,
            true
        );

        // Assert
        assertNotNull(usuario);
        assertEquals(Integer.valueOf(5), usuario.getId());
        assertEquals("Ana Santos", usuario.getNome());
        assertEquals("ana@email.com", usuario.getEmail());
        assertEquals("senha789", usuario.getSenha());
        assertEquals(TipoUsuarioEnum.PRESTADOR_SERVICO, usuario.getTipoUsuario());
        assertTrue(usuario.getAtivo());
    }

    @Test
    @DisplayName("Deve verificar se usuário é dono de restaurante")
    void testVerificarSeEhDonoRestaurante() {
        // Arrange
        Usuario usuarioDono = new Usuario(
            "José Silva",
            "jose@email.com",
            "senha123",
            TipoUsuarioEnum.DONO_RESTAURANTE,
            enderecos
        );

        Usuario usuarioCliente = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Act & Assert
        assertTrue(usuarioDono.getEhDonoRestaurante());
        assertFalse(usuarioCliente.getEhDonoRestaurante());
    }

    @Test
    @DisplayName("Deve comparar usuários por email usando equals")
    void testCompararUsuariosPorEmail() {
        // Arrange
        Usuario usuario1 = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        Usuario usuario2 = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha456",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        Usuario usuario3 = new Usuario(
            "Maria Santos",
            "maria@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Act & Assert
        assertEquals(usuario1, usuario2);
        assertNotEquals(usuario1, usuario3);
    }

    @Test
    @DisplayName("Deve gerar hash code igual para usuários com mesmo email")
    void testHashCodeIgualParaMesmoEmail() {
        // Arrange
        Usuario usuario1 = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        Usuario usuario2 = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha456",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Act & Assert
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    @DisplayName("Deve validar que objeto null não é igual a usuário")
    void testUsuarioNaoEhIgualANull() {
        // Arrange
        Usuario usuario = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Act & Assert
        assertFalse(usuario.equals(null));
    }

    @Test
    @DisplayName("Deve validar que usuário é igual a si mesmo")
    void testUsuarioIgualASiMesmo() {
        // Arrange
        Usuario usuario = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Act & Assert
        assertEquals(usuario, usuario);
    }

    @Test
    @DisplayName("Deve validar que objeto de classe diferente não é igual a usuário")
    void testUsuarioNaoEhIgualAOutraClasse() {
        // Arrange
        Usuario usuario = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Act & Assert
        assertNotEquals(usuario, "João Silva");
    }

    @Test
    @DisplayName("Deve retornar endereços do usuário")
    void testRetornarEnderecosUsuario() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco1 = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        Endereco endereco2 = new Endereco("Rua B", "456", "Apt 202", "Bairro Y", "Ponto Ref", "23456-789", "Rio de Janeiro", "RJ", false, null);
        enderecos.add(endereco1);
        enderecos.add(endereco2);

        Usuario usuario = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Act & Assert
        assertEquals(2, usuario.getEnderecos().size());
        assertEquals(endereco1, usuario.getEnderecos().get(0));
        assertEquals(endereco2, usuario.getEnderecos().get(1));
    }

    @Test
    @DisplayName("Deve criar usuário CLIENTE com sucesso")
    void testCriarUsuarioCliente() {
        // Act
        Usuario usuario = Usuario.create(
            "Cliente Silva",
            "cliente@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Assert
        assertEquals(TipoUsuarioEnum.CLIENTE, usuario.getTipoUsuario());
        assertFalse(usuario.getEhDonoRestaurante());
    }

    @Test
    @DisplayName("Deve criar usuário FORNECEDOR com sucesso")
    void testCriarUsuarioFornecedor() {
        // Act
        Usuario usuario = Usuario.create(
            "Fornecedor Silva",
            "fornecedor@email.com",
            "senha123",
            TipoUsuarioEnum.FORNECEDOR,
            enderecos
        );

        // Assert
        assertEquals(TipoUsuarioEnum.FORNECEDOR, usuario.getTipoUsuario());
        assertFalse(usuario.getEhDonoRestaurante());
    }

    @Test
    @DisplayName("Deve validar email com formato válido")
    void testValidarEmailComFormatoValido() {
        // Act
        Usuario usuario = Usuario.create(
            "João Silva",
            "joao.silva@email.com.br",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Assert
        assertEquals("joao.silva@email.com.br", usuario.getEmail());
    }

    @Test
    @DisplayName("Deve aceitar senha com exatamente 6 caracteres")
    void testAceitarSenhaComExatamente6Caracteres() {
        // Act
        Usuario usuario = Usuario.create(
            "João Silva",
            "joao@email.com",
            "senha1",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Assert
        assertEquals("senha1", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve aceitar nome com exatamente 3 caracteres")
    void testAceitarNomeComExatamente3Caracteres() {
        // Act
        Usuario usuario = Usuario.create(
            "Jão",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );

        // Assert
        assertEquals("Jão", usuario.getNome());
    }
}
