package com.postech.adjt.data.mapper;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioMapper - Testes Unitários")
class UsuarioMapperTest {

    private List<Endereco> enderecos;
    private Endereco endereco;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    @BeforeEach
    void setUp() {
        dataCriacao = LocalDateTime.of(2023, 1, 15, 10, 30, 0);
        dataAlteracao = LocalDateTime.of(2023, 6, 20, 14, 45, 30);

        enderecos = new ArrayList<>();
        endereco = Endereco.builder()
            .logradouro("Rua A")
            .numero("123")
            .complemento("Apt 101")
            .bairro("Bairro X")
            .pontoReferencia("Ponto Ref")
            .cep("12345-678")
            .municipio("São Paulo")
            .uf("SP")
            .principal(false)
            .build();
        enderecos.add(endereco);
    }

    @Test
    @DisplayName("Deve mapear Usuario para UsuarioEntidade com sucesso")
    void testMapearUsuarioParaEntidade() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.CLIENTE)
            .enderecos(enderecos)
            .id(1)
            .dataCriacao(dataCriacao)
            .dataAlteracao(dataAlteracao)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        assertNotNull(entidade);
        assertEquals(Integer.valueOf(1), entidade.getId());
        assertEquals("João Silva", entidade.getNome());
        assertEquals("joao@email.com", entidade.getEmail());
        assertEquals("senha123", entidade.getSenha());
        assertEquals(TipoUsuarioEnum.CLIENTE, entidade.getTipoUsuario());
        assertEquals(dataCriacao, entidade.getDataCriacao());
        assertEquals(dataAlteracao, entidade.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear Usuario null para entidade")
    void testMapearUsuarioNulParaEntidade() {
        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(null);

        // Assert
        assertNull(entidade);
    }

    @Test
    @DisplayName("Deve mapear endereços do Usuario para endereços da entidade")
    void testMapearEnderecosParaEntidade() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Maria Santos")
            .email("maria@email.com")
            .senha("senha456")
            .tipoUsuario(TipoUsuarioEnum.DONO_RESTAURANTE)
            .enderecos(enderecos)
            .id(2)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        assertNotNull(entidade.getEnderecos());
        assertEquals(1, entidade.getEnderecos().size());
        EnderecoEntidade enderecoEntidade = entidade.getEnderecos().get(0);
        assertEquals("Rua A", enderecoEntidade.getLogradouro());
        assertEquals("123", enderecoEntidade.getNumero());
        assertEquals("12345-678", enderecoEntidade.getCep());
    }

    @Test
    @DisplayName("Deve mapear múltiplos endereços para entidade")
    void testMapearMultiplosEnderecosParaEntidade() {
        // Arrange
        List<Endereco> multiploEnderecos = new ArrayList<>();
        Endereco endereco1 = Endereco.builder()
            .logradouro("Rua A")
            .numero("123")
            .complemento("Apt 101")
            .bairro("Bairro X")
            .pontoReferencia("Ponto Ref")
            .cep("12345-678")
            .municipio("São Paulo")
            .uf("SP")
            .principal(true)
            .build();
        Endereco endereco2 = Endereco.builder()
            .logradouro("Rua B")
            .numero("456")
            .complemento("Apt 202")
            .bairro("Bairro Y")
            .pontoReferencia("Ponto Ref")
            .cep("23456-789")
            .municipio("Rio de Janeiro")
            .uf("RJ")
            .principal(false)
            .build();
        multiploEnderecos.add(endereco1);
        multiploEnderecos.add(endereco2);

        Usuario usuario = Usuario.builder()
            .nome("Pedro Costa")
            .email("pedro@email.com")
            .senha("senha789")
            .tipoUsuario(TipoUsuarioEnum.FORNECEDOR)
            .enderecos(multiploEnderecos)
            .id(3)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        assertNotNull(entidade.getEnderecos());
        assertEquals(2, entidade.getEnderecos().size());
        assertEquals("Rua A", entidade.getEnderecos().get(0).getLogradouro());
        assertEquals("Rua B", entidade.getEnderecos().get(1).getLogradouro());
    }

    @Test
    @DisplayName("Deve mapear Usuario com endereços null para entidade")
    void testMapearUsuarioComEnderecosNulParaEntidade() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Ana Silva")
            .email("ana@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.PRESTADOR_SERVICO)
            .enderecos(new ArrayList<>())
            .id(4)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        assertNotNull(entidade);
        assertEquals(Integer.valueOf(4), entidade.getId());
        assertEquals(0, entidade.getEnderecos().size());
    }

    @Test
    @DisplayName("Deve vincular usuarioEntidade aos endereços mapeados")
    void testVincularUsuarioEntidadeAosEnderecos() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("José Silva")
            .email("jose@email.com")
            .senha("senha456")
            .tipoUsuario(TipoUsuarioEnum.CLIENTE)
            .enderecos(enderecos)
            .id(5)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        assertNotNull(entidade.getEnderecos());
        EnderecoEntidade enderecoEntidade = entidade.getEnderecos().get(0);
        assertNotNull(enderecoEntidade.getUsuario());
        assertEquals(entidade, enderecoEntidade.getUsuario());
    }

    @Test
    @DisplayName("Deve mapear UsuarioEntidade para Usuario com sucesso")
    void testMapearEntidadeParaUsuario() {
        // Arrange
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(1);
        entidade.setNome("João Silva");
        entidade.setEmail("joao@email.com");
        entidade.setSenha("senha123");
        entidade.setTipoUsuario(TipoUsuarioEnum.CLIENTE);
        entidade.setAtivo(true);
        entidade.setDataCriacao(dataCriacao);
        entidade.setDataAlteracao(dataAlteracao);

        // Act
        Usuario usuario = UsuarioMapper.toDomain(entidade);

        // Assert
        assertNotNull(usuario);
        assertEquals(Integer.valueOf(1), usuario.getId());
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        assertEquals(TipoUsuarioEnum.CLIENTE, usuario.getTipoUsuario());
        assertTrue(usuario.getAtivo());
        assertEquals(dataCriacao, usuario.getDataCriacao());
        assertEquals(dataAlteracao, usuario.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear UsuarioEntidade null para dominio")
    void testMapearEntidadeNulaParaUsuario() {
        // Act
        Usuario usuario = UsuarioMapper.toDomain(null);

        // Assert
        assertNull(usuario);
    }

    @Test
    @DisplayName("Deve mapear endereços da entidade para dominio")
    void testMapearEnderecosEntidadeParaDominio() {
        // Arrange
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(2);
        entidade.setNome("Maria Santos");
        entidade.setEmail("maria@email.com");
        entidade.setSenha("senha456");
        entidade.setTipoUsuario(TipoUsuarioEnum.DONO_RESTAURANTE);
        entidade.setAtivo(true);

        EnderecoEntidade enderecoEntidade = new EnderecoEntidade();
        enderecoEntidade.setId(1);
        enderecoEntidade.setLogradouro("Rua A");
        enderecoEntidade.setNumero("123");
        enderecoEntidade.setCep("12345-678");
        enderecoEntidade.setMunicipio("São Paulo");
        enderecoEntidade.setUf("SP");

        List<EnderecoEntidade> enderecos = new ArrayList<>();
        enderecos.add(enderecoEntidade);
        entidade.setEnderecos(enderecos);

        // Act
        Usuario usuario = UsuarioMapper.toDomain(entidade);

        // Assert
        assertNotNull(usuario.getEnderecos());
        assertEquals(1, usuario.getEnderecos().size());
        Endereco enderecoDominio = usuario.getEnderecos().get(0);
        assertEquals(Integer.valueOf(1), enderecoDominio.getId());
        assertEquals("Rua A", enderecoDominio.getLogradouro());
        assertEquals("123", enderecoDominio.getNumero());
        assertEquals("12345-678", enderecoDominio.getCep());
    }

    @Test
    @DisplayName("Deve mapear múltiplos endereços da entidade para dominio")
    void testMapearMultiplosEnderecosEntidadeParaDominio() {
        // Arrange
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(3);
        entidade.setNome("Pedro Costa");
        entidade.setEmail("pedro@email.com");
        entidade.setSenha("senha789");
        entidade.setTipoUsuario(TipoUsuarioEnum.FORNECEDOR);
        entidade.setAtivo(true);

        EnderecoEntidade endereco1 = new EnderecoEntidade();
        endereco1.setId(1);
        endereco1.setLogradouro("Rua A");
        endereco1.setNumero("123");
        endereco1.setCep("12345-678");

        EnderecoEntidade endereco2 = new EnderecoEntidade();
        endereco2.setId(2);
        endereco2.setLogradouro("Rua B");
        endereco2.setNumero("456");
        endereco2.setCep("23456-789");

        List<EnderecoEntidade> enderecos = new ArrayList<>();
        enderecos.add(endereco1);
        enderecos.add(endereco2);
        entidade.setEnderecos(enderecos);

        // Act
        Usuario usuario = UsuarioMapper.toDomain(entidade);

        // Assert
        assertNotNull(usuario.getEnderecos());
        assertEquals(2, usuario.getEnderecos().size());
        assertEquals("Rua A", usuario.getEnderecos().get(0).getLogradouro());
        assertEquals("Rua B", usuario.getEnderecos().get(1).getLogradouro());
    }

    @Test
    @DisplayName("Deve mapear UsuarioEntidade com endereços null para dominio")
    void testMapearEntidadeComEnderecosNulaParaDominio() {
        // Arrange
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(4);
        entidade.setNome("Ana Silva");
        entidade.setEmail("ana@email.com");
        entidade.setSenha("senha123");
        entidade.setTipoUsuario(TipoUsuarioEnum.PRESTADOR_SERVICO);
        entidade.setAtivo(true);
        entidade.setEnderecos(null);

        // Act
        Usuario usuario = UsuarioMapper.toDomain(entidade);

        // Assert
        assertNotNull(usuario);
        assertNull(usuario.getEnderecos());
    }

    @Test
    @DisplayName("Deve mapear UsuarioEntidade com endereços vazio para dominio")
    void testMapearEntidadeComEnderecosVazioParaDominio() {
        // Arrange
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(5);
        entidade.setNome("José Silva");
        entidade.setEmail("jose@email.com");
        entidade.setSenha("senha456");
        entidade.setTipoUsuario(TipoUsuarioEnum.CLIENTE);
        entidade.setAtivo(true);
        entidade.setEnderecos(new ArrayList<>());

        // Act
        Usuario usuario = UsuarioMapper.toDomain(entidade);

        // Assert
        assertNotNull(usuario);
        assertNotNull(usuario.getEnderecos());
        assertTrue(usuario.getEnderecos().isEmpty());
    }

    @Test
    @DisplayName("Deve preservar dados ao mapear bidirecional (Usuario -> Entidade -> Usuario)")
    void testMapearBidirecional() {
        // Arrange
        Usuario usuarioOriginal = Usuario.builder()
            .nome("Lucas Silva")
            .email("lucas@email.com")
            .senha("senha789")
            .tipoUsuario(TipoUsuarioEnum.FORNECEDOR)
            .enderecos(enderecos)
            .id(10)
            .dataCriacao(dataCriacao)
            .dataAlteracao(dataAlteracao)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuarioOriginal);
        Usuario usuarioMapeado = UsuarioMapper.toDomain(entidade);

        // Assert
        assertEquals(usuarioOriginal.getId(), usuarioMapeado.getId());
        assertEquals(usuarioOriginal.getNome(), usuarioMapeado.getNome());
        assertEquals(usuarioOriginal.getEmail(), usuarioMapeado.getEmail());
        assertEquals(usuarioOriginal.getSenha(), usuarioMapeado.getSenha());
        assertEquals(usuarioOriginal.getTipoUsuario(), usuarioMapeado.getTipoUsuario());
        assertEquals(usuarioOriginal.getDataCriacao(), usuarioMapeado.getDataCriacao());
        assertEquals(usuarioOriginal.getDataAlteracao(), usuarioMapeado.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve mapear Usuario com tipo DONO_RESTAURANTE para entidade")
    void testMapearUsuarioDonoRestauranteParaEntidade() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Restaurante Silva")
            .email("restaurante@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.DONO_RESTAURANTE)
            .enderecos(enderecos)
            .id(6)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, entidade.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve mapear Usuario com ativo false para entidade")
    void testMapearUsuarioInativoParaEntidade() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.CLIENTE)
            .enderecos(enderecos)
            .id(7)
            .ativo(false)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        assertFalse(entidade.getAtivo());
    }

    @Test
    @DisplayName("Deve manter relacionamento entre EnderecoEntidade e UsuarioEntidade")
    void testMantendoRelacionamentoEnderecoUsuario() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Teste Silva")
            .email("teste@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.CLIENTE)
            .enderecos(enderecos)
            .id(8)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        for (EnderecoEntidade enderecoEnt : entidade.getEnderecos()) {
            assertNotNull(enderecoEnt.getUsuario());
            assertEquals(entidade.getId(), enderecoEnt.getUsuario().getId());
        }
    }

    @Test
    @DisplayName("Deve mapear Usuario sem endereços para entidade")
    void testMapearUsuarioSemEnderecosParaEntidade() {
        // Arrange
        Usuario usuario = Usuario.builder()
            .nome("Sem Endereço")
            .email("semendereco@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.CLIENTE)
            .enderecos(new ArrayList<>())
            .id(9)
            .build();

        // Act
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

        // Assert
        assertNotNull(entidade);
        assertTrue(entidade.getEnderecos().isEmpty());
    }
}
